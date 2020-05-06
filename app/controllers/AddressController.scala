package controllers

import javax.inject.{Inject, Singleton}
import models.{Address, AddressRepository}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressController @Inject()(cc: MessagesControllerComponents, addressRepository: AddressRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createAddressForm: Form[CreateAddressForm] = Form {
    mapping(
      "street" -> nonEmptyText,
      "building" -> number,
      "city" -> nonEmptyText,
      "postalCode" -> nonEmptyText
    )(CreateAddressForm.apply)(CreateAddressForm.unapply)
  }

  val updateAddressForm: Form[UpdateAddressForm] = Form {
    mapping(
      "id" -> number,
      "street" -> nonEmptyText,
      "building" -> number,
      "city" -> nonEmptyText,
      "postalCode" -> nonEmptyText
    )(UpdateAddressForm.apply)(UpdateAddressForm.unapply)
  }

  def addAddress = Action.async { implicit request =>
    Future.successful(Ok(views.html.address.addressAdd(createAddressForm)))
  }

  def addAddressHandle = Action.async { implicit request =>
    createAddressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.address.addressAdd(errorForm))
        )
      },
      addr => {
        addressRepository.create(addr.street, addr.building, addr.city, addr.postalCode).map { _ =>
          Redirect(routes.AddressController.addAddress()).flashing("success" -> "address created")
        }
      }
    )
  }

  def updateAddress(id: Int) = Action.async { implicit request =>
    val address = addressRepository.getById(id)
    address.map(addr => {
      val addrForm = updateAddressForm.fill(UpdateAddressForm(addr.id, addr.street, addr.building, addr.city, addr.postalCode))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.address.addressUpdate(addrForm))
    })
  }

  def updateAddressHandle = Action.async { implicit request =>
    updateAddressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.address.addressUpdate(errorForm))
        )
      },
      addr => {
        addressRepository.update(addr.id, Address(addr.id, addr.street, addr.building, addr.city, addr.postalCode)).map { _ =>
          Redirect(routes.AddressController.updateAddress(addr.id)).flashing("success" -> "address updated")
        }
      }
    )
  }

  def deleteAddress(id: Int) = Action {
    addressRepository.delete(id)
    Redirect(routes.AddressController.getAllAddresses())
  }

  def getAddress(id: Int) = Action.async { implicit request =>
    addressRepository.getByIdOption(id).map {
      case Some(a) => Ok(views.html.address.address(a))
      case None => Redirect(routes.AddressController.getAllAddresses())
    }
  }

  def getAllAddresses = Action.async { implicit request =>
    addressRepository.list().map(addr => Ok(views.html.address.addresses(addr)))
  }

}

case class CreateAddressForm(street: String, building: Int, city: String, postalCode: String)
case class UpdateAddressForm(id: Int, street: String, building: Int, city: String, postalCode: String)