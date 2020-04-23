package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addOrder = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addOrderHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateOrder(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateOrderHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteOrder(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getOrder(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllOrders = Action {
    Ok(views.html.index("asd"))
  }
}