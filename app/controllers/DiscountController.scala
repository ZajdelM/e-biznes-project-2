package controllers

import javax.inject.{Inject, Singleton}
import models.DiscountRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class DiscountController @Inject()(cc: ControllerComponents, discountRepository: DiscountRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addDiscount = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addDiscountHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateDiscount(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateDiscountHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteDiscount(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getDiscount(id: Int) = Action.async { implicit request =>
    discountRepository.getByIdOption(id).map {
        case Some(n) => Ok(views.html.discount(n))
        case None => Redirect(routes.DiscountController.getAllDiscounts())
      }
  }

  def getAllDiscounts = Action.async { implicit request =>
    discountRepository.list().map(discounts => Ok(views.html.discounts(discounts)))
  }
}
