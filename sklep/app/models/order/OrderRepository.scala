package models.order

import javax.inject.{Inject, Singleton}
import models.client.ClientRepository
import models.payment.PaymentRepository
import models.shipment.ShipmentRepository
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val paymentRepository: PaymentRepository, val shipmentRepository: ShipmentRepository, val clientRepository: ClientRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def client = column[Int]("client")
    def client_fk = foreignKey("client_fk", client, cli)(_.id)
    def payment = column[Int]("payment")
    def payment_fk = foreignKey("payment_fk", payment, pay)(_.id)
    def shipment = column[Int]("shipment")
    def shipment_fk = foreignKey("shipment_fk", shipment, ship)(_.id)

    def * = (id, client, payment, shipment) <> ((Order.apply _).tupled, Order.unapply)

  }

  import clientRepository.ClientTable
  import paymentRepository.PaymentTable
  import shipmentRepository.ShipmentTable

  private val order = TableQuery[OrderTable]
  private val cli = TableQuery[ClientTable]
  private val pay = TableQuery[PaymentTable]
  private val ship = TableQuery[ShipmentTable]

  def create(client: Int, payment: Int, shipment: Int): Future[Order] = db.run {
    (order.map(o => (o.client, o.payment, o.shipment))
      returning order.map(_.id)
      into { case ((client, payment, shipment), id) => Order(id, client, payment, shipment) }
      ) += (client, payment, shipment)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }

  def getById(id: Int): Future[Order] = db.run {
    order.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(order.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_order: Order): Future[Order] = {
    val orderToUpdate: Order = new_order.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => new_order)
  }
}
