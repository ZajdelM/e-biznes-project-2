package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class DiscountsController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addDiscount = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addDiscountHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateDiscount(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateDiscountHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteDiscount(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getDiscount(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllDiscounts = Action {
    Ok(views.html.index("asd"))
  }
}
