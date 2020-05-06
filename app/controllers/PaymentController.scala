package controllers

import javax.inject.{Inject, Singleton}
import models.{Payment, PaymentRepository}
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

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