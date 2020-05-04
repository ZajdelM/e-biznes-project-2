package controllers

import javax.inject.{Inject, Singleton}
import models.CartRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class CartController @Inject()(cc: ControllerComponents, cartRepository: CartRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addCart = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addCartHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCart(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCartHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteCart(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getCart(id: Int) = Action.async { implicit request =>
    cartRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.cart(c))
      case None => Redirect(routes.CartController.getAllCarts())
    }
  }

  def getAllCarts = Action.async { implicit request =>
    cartRepository.list().map(carts => Ok(views.html.carts(carts)))
  }
}
