package controllers

import javax.inject.{Inject, Singleton}
import models.ShipmentRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class ShipmentController @Inject()(cc: ControllerComponents, shipmentRepository: ShipmentRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addShipment = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addShipmentHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateShipment(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateShipmentHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteShipment(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getShipment(id: Int) = Action.async { implicit request =>
    shipmentRepository.getByIdOption(id).map {
      case Some(s) => Ok(views.html.shipment(s))
      case None => Redirect(routes.ShipmentController.getAllShipments())
    }
  }

  def getAllShipments = Action.async { implicit request =>
    shipmentRepository.list().map(ship => Ok(views.html.shipments(ship)))
  }
}
