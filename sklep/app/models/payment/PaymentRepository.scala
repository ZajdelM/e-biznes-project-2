package models.payment

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def amount = column[Double]("amount")
    def method = column[String]("method")
    def * = (id, amount, method) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  private val payment = TableQuery[PaymentTable]

  def create(amount: Double, method: String): Future[Payment] = db.run {
    (payment.map(p => (p.amount, p.method))
      returning payment.map(_.id)
      into { case ((amount, method), id) => Payment(id, amount, method) }
      ) += (amount, method)
  }

  /**
   * List all the people in the database
   */
  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getByMethod(method: String): Future[Seq[Payment]] = db.run {
    payment.filter(_.method === method).result
  }

  def getById(id: Int): Future[Payment] = db.run {
    payment.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(payment.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_payment: Payment): Future[Payment] = {
    val paymentToUpdate: Payment = new_payment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate)).map(_ => new_payment)
  }
}

