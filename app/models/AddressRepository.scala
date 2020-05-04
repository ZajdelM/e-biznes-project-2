package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class AddressRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AddressTable(tag: Tag) extends Table[Address](tag, "address") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def street = column[String]("street")
    def building = column[Int]("building")
    def city = column[String]("city")
    def postalCode = column[String]("postalCode")

    def * = (id, street, building, city, postalCode) <> ((Address.apply _).tupled, Address.unapply)
  }

  private val address = TableQuery[AddressTable]

  def create(street: String, building: Int, city: String, postalCode: String): Future[Address] = db.run {
    (address.map(a => (a.street, a.building, a.city, a.postalCode))
      returning address.map(_.id)
      into {case ((street, building, city, postalCode), id) => Address(id, street, building, city, postalCode)}
      ) += (street, building, city, postalCode)
  }

  def list(): Future[Seq[Address]] = db.run {
    address.result
  }

  def getById(id: Int): Future[Address] = db.run {
    address.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Address]] = db.run {
    address.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(address.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_address: Address): Future[Unit] = {
    val productToUpdate: Address = new_address.copy(id)
    db.run(address.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }
}
