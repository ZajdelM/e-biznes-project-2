package controllers

import javax.inject.{Inject, Singleton}
import models.{ClientRepository, Order, OrderRepository, PaymentRepository, ShipmentRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderController @Inject()(cc: MessagesControllerComponents,
                                orderRepository: OrderRepository,
                                clientRepository: ClientRepository,
                                paymentRepository: PaymentRepository,
                                shipmentRepository: ShipmentRepository)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createOrderForm: Form[CreateOrderForm] = Form {
    mapping(
      "client" -> number,
      "payment" -> number,
      "shipment" -> number
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> number,
      "client" -> number,
      "payment" -> number,
      "shipment" -> number
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  def addOrder = Action.async { implicit request: MessagesRequest[AnyContent] =>
    clientRepository.list().flatMap(clients =>
      paymentRepository.list().flatMap(payments =>
        shipmentRepository.list().map(shipments =>
          Ok(views.html.order.orderAdd(createOrderForm, clients, payments, shipments))
        )
      )
    )
  }

  def addOrderHandle = Action.async { implicit request: MessagesRequest[AnyContent] =>
    clientRepository.list().flatMap(clients =>
      paymentRepository.list().flatMap(payments =>
        shipmentRepository.list().flatMap(shipments =>
          createOrderForm.bindFromRequest.fold(
            errorForm => {
              Future.successful(
                BadRequest(views.html.order.orderAdd(errorForm, clients, payments, shipments))
              )
            },
            order => {
              orderRepository.create(order.client, order.payment, order.shipment).map { _ =>
                Redirect(routes.OrderController.addOrder()).flashing("success" -> "order created")
              }
            }
          )
        )
      )
    )
  }

  def updateOrder(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    clientRepository.list().flatMap(clients =>
      paymentRepository.list().flatMap(payments =>
        shipmentRepository.list().flatMap(shipments =>
          orderRepository.getById(id).map(order => {
            val orderForm = updateOrderForm.fill(UpdateOrderForm(order.id, order.client, order.payment, order.shipment))
            Ok(views.html.order.orderUpdate(orderForm, clients, payments, shipments))
          }
          )
        )
      )
    )
  }

  def updateOrderHandle = Action.async { implicit request =>
    clientRepository.list().flatMap(clients =>
      paymentRepository.list().flatMap(payments =>
        shipmentRepository.list().flatMap(shipments =>
          updateOrderForm.bindFromRequest.fold(
            errorForm => {
              Future.successful(
                BadRequest(views.html.order.orderUpdate(errorForm, clients, payments, shipments))
              )
            },
            order => {
              orderRepository.update(order.id, Order(order.id, order.client, order.payment, order.shipment)).map { _ =>
                Redirect(routes.OrderController.updateOrder(order.id)).flashing("success" -> "order updated")
              }
            }
          )
        )
      )
    )
  }

  def deleteOrder(id: Int) = Action {
    orderRepository.delete(id)
    Redirect(routes.OrderController.getAllOrders())
  }

  def getOrder(id: Int) = Action.async { implicit request =>
    orderRepository.getByIdOption(id).map {
      case Some(o) => Ok(views.html.order.order(o))
      case None => Redirect(routes.OrderController.getAllOrders())
    }
  }

  def getAllOrders = Action.async { implicit request =>
    orderRepository.list().map(orders => Ok(views.html.order.orders(orders)))
  }

}

case class CreateOrderForm(client: Int, payment: Int, shipment: Int)

case class UpdateOrderForm(id: Int, client: Int, payment: Int, shipment: Int)