package controllers

import javax.inject.{Inject, Singleton}
import models.PaymentRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class PaymentController @Inject()(cc: ControllerComponents, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addPayment = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addPaymentHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updatePayment(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updatePaymentHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deletePayment(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getPayment(id: Int) = Action.async { implicit request =>
    paymentRepository.getByIdOption(id).map {
      case Some(p) => Ok(views.html.payment(p))
      case None => Redirect(routes.PaymentController.getAllPayments())
    }
  }

  def getAllPayments = Action.async { implicit request =>
    paymentRepository.list().map(paym => Ok(views.html.payments(paym)))
  }
}
