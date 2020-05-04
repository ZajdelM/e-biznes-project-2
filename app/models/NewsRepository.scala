package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NewsRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class NewsTable(tag: Tag) extends Table[News](tag, "news") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def message = column[String]("message")

    def * = (id, message) <> ((News.apply _).tupled, News.unapply)
  }

  private val news = TableQuery[NewsTable]

  def create(message: String): Future[News] = db.run {
    (news.map(n => (n.message))
      returning news.map(_.id)
      into ((message, id) => News(id, message))
      ) += (message)
  }

  def list(): Future[Seq[News]] = db.run {
    news.result
  }

  def getById(id: Int): Future[News] = db.run {
    news.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[News]] = db.run {
    news.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Unit] = db.run(news.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_news: News): Future[Unit] = {
    val newsToUpdate: News = new_news.copy(id)
    db.run(news.filter(_.id === id).update(newsToUpdate)).map(_ => ())
  }
}
