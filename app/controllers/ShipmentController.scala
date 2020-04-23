package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ShipmentController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addShipment = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addShipmentHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateShipment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateShipmentHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteShipment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getShipment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllShipments = Action {
    Ok(views.html.index("asd"))
  }
}
