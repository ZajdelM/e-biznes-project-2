package models


import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val addressRepository: AddressRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ClientTable(tag: Tag) extends Table[Client](tag, "client") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")
    def surname = column[String]("surname")
    def address = column[Int]("address")
    def address_fk = foreignKey("address_fk", address, addr)(_.id)

    def * = (id, name, surname, address) <> ((Client.apply _).tupled, Client.unapply)
  }

  import addressRepository.AddressTable

  private val client = TableQuery[ClientTable]
  private val addr = TableQuery[AddressTable]

  def create(name: String, surname: String, address: Int): Future[Client] = db.run {
    (client.map(c => (c.name, c.surname, c.address))
      returning client.map(_.id)
      into { case ((name, surname, address), id) => Client(id, name, surname, address) }
      ) += (name, surname, address)
  }

  def list(): Future[Seq[Client]] = db.run {
    client.result
  }

  def getByCategory(address_id: Int): Future[Seq[Client]] = db.run {
    client.filter(_.address === address_id).result
  }

  def getById(id: Int): Future[Client] = db.run {
    client.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Client]] = db.run {
    client.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(client.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_client: Client): Future[Unit] = {
    val clientToUpdate: Client = new_client.copy(id)
    db.run(client.filter(_.id === id).update(clientToUpdate)).map(_ => ())
  }
}

