package controllers

import javax.inject.{Inject, Singleton}
import models.OrderRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class OrderController @Inject()(cc: ControllerComponents, orderRepository: OrderRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addOrder = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addOrderHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateOrder(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateOrderHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteOrder(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getOrder(id: Int) = Action.async { implicit request =>
    orderRepository.getByIdOption(id).map {
      case Some(o) => Ok(views.html.order(o))
      case None => Redirect(routes.OrderController.getAllOrders())
    }
  }

  def getAllOrders = Action.async { implicit request =>
    orderRepository.list().map(orders => Ok(views.html.orders(orders)))
  }
}