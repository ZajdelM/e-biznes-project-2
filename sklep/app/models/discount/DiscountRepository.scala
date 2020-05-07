package models.discount

import javax.inject.{Inject, Singleton}
import models.product.ProductRepository
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DiscountRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class DiscountTable(tag: Tag) extends Table[Discount](tag, "discount") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def discount_size = column[Int]("discount_size")
    def product = column[Int]("product")
    def product_fk = foreignKey("product_fk", product, prod)(_.id)

    def * = (id, discount_size, product) <> ((Discount.apply _).tupled, Discount.unapply)
  }

  import productRepository.ProductTable

  private val discount = TableQuery[DiscountTable]
  private val prod = TableQuery[ProductTable]

  def create(discount_size: Int, product: Int): Future[Discount] = db.run {
    (discount.map(d => (d.discount_size, d.product))
      returning discount.map(_.id)
      into { case ((discount_size, product), id) => Discount(id, discount_size, product) }
      ) += (discount_size, product)
  }

  def list(): Future[Seq[Discount]] = db.run {
    discount.result
  }

  def getByProduct(product_id: Int): Future[Seq[Discount]] = db.run {
    discount.filter(_.product === product_id).result
  }

  def getById(id: Int): Future[Discount] = db.run {
    discount.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Discount]] = db.run {
    discount.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(discount.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_discount: Discount): Future[Discount] = {
    val discountToUpdate: Discount = new_discount.copy(id)
    db.run(discount.filter(_.id === id).update(discountToUpdate)).map(_ => new_discount)
  }

}

