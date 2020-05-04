package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val productRepository: ProductRepository, val clientRepository: ClientRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CartTable(tag: Tag) extends Table[Cart](tag, "cart") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def product = column[Int]("product")
    def product_fk = foreignKey("prod_fk", product, prod)(_.id)
    def quantity = column[Int]("quantity")
    def client = column[Int]("client")
    def client_fk = foreignKey("client_fk", client, cli)(_.id)

    def * = (id, product, quantity, client) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import productRepository.ProductTable
  import clientRepository.ClientTable

  private val cart = TableQuery[CartTable]
  private val cli = TableQuery[ClientTable]
  private val prod = TableQuery[ProductTable]

  def create(product: Int, quantity: Int, client: Int): Future[Cart] = db.run {
    (cart.map(c => (c.product, c.quantity, c.client))
      returning cart.map(_.id)
      into { case ((product, quantity, client), id) => Cart(id, product, quantity, client) }
      ) += (product, quantity, client)
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def getByClient(client_id: Int): Future[Seq[Cart]] = db.run {
    cart.filter(_.client === client_id).result
  }

  def getById(id: Int): Future[Cart] = db.run {
    cart.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(cart.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_cart: Cart): Future[Unit] = {
    val cartToUpdate: Cart = new_cart.copy(id)
    db.run(cart.filter(_.id === id).update(cartToUpdate)).map(_ => ())
  }
}
