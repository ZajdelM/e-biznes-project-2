package controllers

import javax.inject.{Inject, Singleton}
import models.NewsRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext


@Singleton
class NewsController @Inject()(cc: ControllerComponents, newsRepository: NewsRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addNews = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addNewsHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateNews(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateNewsHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteNews(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getNews(id: Int) = Action.async { implicit request =>
    newsRepository.getByIdOption(id).map {
      case Some(n) => Ok(views.html.news(n))
      case None => Redirect(routes.NewsController.getAllNews())
    }
  }

  def getAllNews = Action.async { implicit request =>
    newsRepository.list().map(newsList => Ok(views.html.newslist(newsList)))
  }
}
