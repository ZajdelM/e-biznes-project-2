package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}


@Singleton
class NewsController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addNews = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addNewsHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateNews(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateNewsHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteNews(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getNews(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllNews = Action {
    Ok(views.html.index("asd"))
  }
}
