package controllers

import javax.inject.{Inject, Singleton}
import models.AddressRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class AddressController @Inject()(cc: ControllerComponents, addressRepository: AddressRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addAddress = Action {
    Ok("test")
  }

  def addAddressHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateAddress(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateAddressHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteAddress(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAddress(id: Int) = Action.async { implicit request =>
    addressRepository.getByIdOption(id).map {
      case Some(a) => Ok(views.html.address(a))
      case None => Redirect(routes.AddressController.getAllAddresses())
    }
  }

  def getAllAddresses = Action.async { implicit request =>
    addressRepository.list().map(addr => Ok(views.html.addresses(addr)))
  }

}
