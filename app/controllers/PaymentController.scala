package controllers

import javax.inject.{Inject, Singleton}
import models.payment.{CreatePayment, Payment, PaymentRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createPaymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "amount" -> of(doubleFormat),
      "method" -> nonEmptyText
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> number,
      "amount" -> of(doubleFormat),
      "method" -> nonEmptyText
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def getAllPaymentsJson(): Action[AnyContent] = Action.async {
    paymentRepository.list()
      .map(payments => Json.toJson(payments))
      .map(jsonPayments => Ok(jsonPayments))
  }

  def getPaymentsByIdJson(id: Int): Action[AnyContent] = Action.async {
    paymentRepository.getByIdOption(id)
      .map {
        case Some(p) => Ok(Json.toJson(p))
        case None => NotFound
      }
  }

  def addPaymentJson: Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val createPaymentJson = json.as[CreatePayment]

    paymentRepository.create(createPaymentJson.amount, createPaymentJson.method).map(
      createdPayment => Created(Json.toJson(createdPayment))
    )
  }

  def updatePaymentJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val updatePaymentJson = json.as[Payment]

    paymentRepository.getById(id).flatMap(existingPayment =>
      paymentRepository.update(existingPayment.id, updatePaymentJson).map(
        updatedPayment => Ok(Json.toJson(updatedPayment))
      )
    )
  }

  def deletePaymentJson(id: Int): Action[AnyContent] = Action.async {
    paymentRepository.delete(id)
    Future(NoContent)
  }

  def addPayment = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future.successful(Ok(views.html.payment.paymentAdd(createPaymentForm)))
  }

  def addPaymentHandle = Action.async { implicit request =>
    createPaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payment.paymentAdd(errorForm))
        )
      },
      payment => {
        paymentRepository.create(payment.amount, payment.method).map { _ =>
          Redirect(routes.PaymentController.addPayment()).flashing("success" -> "payment created")
        }
      }
    )
  }

  def updatePayment(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payment = paymentRepository.getById(id)
    payment.map(paym => {
      val paymForm = updatePaymentForm.fill(UpdatePaymentForm(paym.id, paym.amount, paym.method))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.payment.paymentUpdate(paymForm))
    })
  }

  def updatePaymentHandle = Action.async { implicit request =>
    updatePaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payment.paymentUpdate(errorForm))
        )
      },
      paym => {
        paymentRepository.update(paym.id, Payment(paym.id, paym.amount, paym.method)).map { _ =>
          Redirect(routes.PaymentController.updatePayment(paym.id)).flashing("success" -> "payment updated")
        }
      }
    )
  }

  def deletePayment(id: Int) = Action {
    paymentRepository.delete(id)
    Redirect(routes.PaymentController.getAllPayments())
  }

  def getPayment(id: Int) = Action.async { implicit request =>
    paymentRepository.getByIdOption(id).map {
      case Some(p) => Ok(views.html.payment.payment(p))
      case None => Redirect(routes.PaymentController.getAllPayments())
    }
  }

  def getAllPayments = Action.async { implicit request =>
    paymentRepository.list().map(paym => Ok(views.html.payment.payments(paym)))
  }

}

case class CreatePaymentForm(amount: Double, method: String)

case class UpdatePaymentForm(id: Int, amount: Double, method: String)