package controllers

import javax.inject.{Inject, Singleton}
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class ShipmentController @Inject()(cc: MessagesControllerComponents, shipmentRepository: ShipmentRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createShipmentForm: Form[CreateShipmentForm] = Form {
    mapping(
      "status" -> nonEmptyText,
      "method" -> nonEmptyText
    )(CreateShipmentForm.apply)(CreateShipmentForm.unapply)
  }

  val updateShipmentForm: Form[UpdateShipmentForm] = Form {
    mapping(
      "id" -> number,
      "status" -> nonEmptyText,
      "method" -> nonEmptyText
    )(UpdateShipmentForm.apply)(UpdateShipmentForm.unapply)
  }

  def addShipment = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful(Ok(views.html.shipment.shipmentAdd(createShipmentForm)))
  }

  def addShipmentHandle = Action.async { implicit request =>
    createShipmentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shipment.shipmentAdd(errorForm))
        )
      },
      shipment => {
        shipmentRepository.create(shipment.status, shipment.method).map { _ =>
          Redirect(routes.ShipmentController.addShipment()).flashing("success" -> "shipment created")
        }
      }
    )
  }

  def updateShipment(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val shipment = shipmentRepository.getById(id)
    shipment.map(ship => {
      val shipForm = updateShipmentForm.fill(UpdateShipmentForm(ship.id, ship.status, ship.method))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.shipment.shipmentUpdate(shipForm))
    })
  }

  def updateShipmentHandle = Action.async { implicit request =>
    updateShipmentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shipment.shipmentUpdate(errorForm))
        )
      },
      ship => {
        shipmentRepository.update(ship.id, Shipment(ship.id, ship.status, ship.method)).map { _ =>
          Redirect(routes.ShipmentController.updateShipment(ship.id)).flashing("success" -> "shipment updated")
        }
      }
    )
  }

  def deleteShipment(id: Int) = Action {
    shipmentRepository.delete(id)
    Redirect(routes.ShipmentController.getAllShipments())
  }

  def getShipment(id: Int) = Action.async { implicit request =>
    shipmentRepository.getByIdOption(id).map {
      case Some(s) => Ok(views.html.shipment.shipment(s))
      case None => Redirect(routes.ShipmentController.getAllShipments())
    }
  }

  def getAllShipments = Action.async { implicit request =>
    shipmentRepository.list().map(ship => Ok(views.html.shipment.shipments(ship)))
  }
}

case class CreateShipmentForm(status: String, method: String)
case class UpdateShipmentForm(id: Int, status: String, method: String)