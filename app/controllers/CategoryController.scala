package controllers

import javax.inject.{Inject, Singleton}
import models.CategoryRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject()(cc: ControllerComponents, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addCategory = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addCategoryHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCategory(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCategoryHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteCategory(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getCategory(id: Int) = Action.async { implicit request =>
    categoryRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.category(c))
      case None => Redirect(routes.AddressController.getAllAddresses())
    }
  }

  def getAllCategories = Action.async { implicit request =>
    categoryRepository.list().map(cate => Ok(views.html.categories(cate)))
  }
}
