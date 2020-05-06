package controllers

import javax.inject.{Inject, Singleton}
import models.{News, NewsRepository}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class NewsController @Inject()(cc: MessagesControllerComponents, newsRepository: NewsRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  val createNewsForm: Form[CreateNewsForm] = Form {
    mapping(
      "message" -> nonEmptyText
    )(CreateNewsForm.apply)(CreateNewsForm.unapply)
  }

  val updateNewsForm: Form[UpdateNewsForm] = Form {
    mapping(
      "id" -> number,
      "message" -> nonEmptyText
    )(UpdateNewsForm.apply)(UpdateNewsForm.unapply)
  }

  def addNews = Action.async { implicit request =>
    Future.successful(Ok(views.html.news.newsAdd(createNewsForm)))
  }

  def addNewsHandle = Action.async { implicit request =>
    createNewsForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.news.newsAdd(errorForm))
        )
      },
      news => {
        newsRepository.create(news.message).map { _ =>
          Redirect(routes.NewsController.addNews()).flashing("success" -> "news created")
        }
      }
    )
  }

  def updateNews(id: Int) = Action.async { implicit request =>
    val news = newsRepository.getById(id)
    news.map(paym => {
      val newsForm = updateNewsForm.fill(UpdateNewsForm(paym.id, paym.message))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.news.newsUpdate(newsForm))
    })
  }

  def updateNewsHandle = Action.async { implicit request =>
    updateNewsForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.news.newsUpdate(errorForm))
        )
      },
      news => {
        newsRepository.update(news.id, News(news.id, news.message)).map { _ =>
          Redirect(routes.NewsController.updateNews(news.id)).flashing("success" -> "news updated")
        }
      }
    )
  }

  def deleteNews(id: Int) = Action {
    newsRepository.delete(id)
    Redirect(routes.NewsController.getAllNews())
  }

  def getNews(id: Int) = Action.async { implicit request =>
    newsRepository.getByIdOption(id).map {
      case Some(n) => Ok(views.html.news.news(n))
      case None => Redirect(routes.NewsController.getAllNews())
    }
  }

  def getAllNews = Action.async { implicit request =>
    newsRepository.list().map(newsList => Ok(views.html.news.newslist(newsList)))
  }
}

case class CreateNewsForm(message: String)
case class UpdateNewsForm(id: Int, message: String)