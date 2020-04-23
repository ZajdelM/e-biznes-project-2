package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class CartController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addCart = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addCartHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCart(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateCartHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteCart(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getCart(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllCarts = Action {
    Ok(views.html.index("asd"))
  }
}
