package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class AddressController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def addAddress = Action {
    Ok("test")
  }

  def addAddressHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateAddress(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateAddressHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteAddress(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAddress(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllAddresses = Action {
    Ok(views.html.index("asd"))
  }

}
