package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addCategory = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addCategoryHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCategory(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCategoryHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteCategory(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getCategory(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllCategories = Action {
    Ok(views.html.index("asd"))
  }
}
