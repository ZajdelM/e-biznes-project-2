package controllers

import javax.inject.{Inject, Singleton}
import models._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ProductController @Inject()(cc: ControllerComponents, productRepository: ProductRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addProduct = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addProductHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateProduct(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateProductHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteProduct(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getProduct(id: Int) = Action.async { implicit request =>
    productRepository.getByIdOption(id).map {
        case Some(p) => Ok(views.html.product(p))
        case None => Redirect(routes.ProductController.getAllProducts())
      }
  }

  def getAllProducts = Action.async { implicit request =>
    productRepository.list().map(prod => Ok(views.html.products(prod)))
  }
}
