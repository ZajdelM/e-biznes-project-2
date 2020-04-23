package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class PaymentController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addPayment = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addPaymentHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updatePayment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updatePaymentHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deletePayment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getPayment(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllPayments = Action {
    Ok(views.html.index("asd"))
  }
}
