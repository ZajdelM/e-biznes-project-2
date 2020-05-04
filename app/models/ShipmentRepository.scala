package models


import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShipmentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ShipmentTable(tag: Tag) extends Table[Shipment](tag, "shipment") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def status = column[String]("status")
    def method = column[String]("method")

    def * = (id, status, method) <> ((Shipment.apply _).tupled, Shipment.unapply)
  }

  private val shipment = TableQuery[ShipmentTable]

  def create(status: String, method: String): Future[Shipment] = db.run {
    (shipment.map(s => (s.status, s.method))
      returning shipment.map(_.id)
      into { case ((status, method), id) => Shipment(id, status, method) }
      ) += (status, method)
  }

  def list(): Future[Seq[Shipment]] = db.run {
    shipment.result
  }

  def getById(id: Int): Future[Shipment] = db.run {
    shipment.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Shipment]] = db.run {
    shipment.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(shipment.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_shipment: Shipment): Future[Unit] = {
    val shipmentToUpdate: Shipment = new_shipment.copy(id)
    db.run(shipment.filter(_.id === id).update(shipmentToUpdate)).map(_ => ())
  }
}

