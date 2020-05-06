package controllers

import javax.inject.{Inject, Singleton}
import models.{Cart, CartRepository, ClientRepository, ProductRepository}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartController @Inject()(cc: MessagesControllerComponents, cartRepository: CartRepository, productRepository: ProductRepository, clientRepository: ClientRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  val createCartForm: Form[CreateCartForm] = Form {
    mapping(
      "product" -> number,
      "quantity" -> number,
      "client" -> number
    )(CreateCartForm.apply)(CreateCartForm.unapply)
  }

  val updateCartForm: Form[UpdateCartForm] = Form {
    mapping(
      "id" -> number,
      "product" -> number,
      "quantity" -> number,
      "client" -> number
    )(UpdateCartForm.apply)(UpdateCartForm.unapply)
  }

  def addCart = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.list().flatMap(products =>
      clientRepository.list().map(clients =>
          Ok(views.html.cart.cartAdd(createCartForm,products, clients))
      )
    )
  }

  def addCartHandle = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.list().flatMap(products =>
      clientRepository.list().flatMap(clients =>
        createCartForm.bindFromRequest.fold(
          errorForm => {
            Future.successful(
              BadRequest(views.html.cart.cartAdd(errorForm, products, clients))
            )
          },
          cart => {
            cartRepository.create(cart.product, cart.quantity, cart.client).map { _ =>
              Redirect(routes.CartController.addCart()).flashing("success" -> "cart created")
            }
          }
        )
      )
    )
  }

  def updateCart(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.list().flatMap(products =>
      clientRepository.list().flatMap(clients =>
        cartRepository.getById(id).map(cart => {
            val cartForm = updateCartForm.fill(UpdateCartForm(cart.id, cart.product, cart.quantity, cart.client))
            Ok(views.html.cart.cartUpdate(cartForm, products, clients))
          }
        )
      )
    )
  }

  def updateCartHandle = Action.async { implicit request =>
    productRepository.list().flatMap(products =>
      clientRepository.list().flatMap(clients =>
        updateCartForm.bindFromRequest.fold(
          errorForm => {
            Future.successful(
              BadRequest(views.html.cart.cartUpdate(errorForm, products, clients))
            )
          },
          cart => {
            cartRepository.update(cart.id, Cart(cart.id, cart.product, cart.quantity, cart.client)).map { _ =>
              Redirect(routes.CartController.updateCart(cart.id)).flashing("success" -> "cart updated")
            }
          }
        )
      )
    )
  }

  def deleteCart(id: Int) = Action {
    cartRepository.delete(id)
    Redirect(routes.CartController.getAllCarts())
  }

  def getCart(id: Int) = Action.async { implicit request =>
    cartRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.cart.cart(c))
      case None => Redirect(routes.CartController.getAllCarts())
    }
  }

  def getAllCarts = Action.async { implicit request =>
    cartRepository.list().map(carts => Ok(views.html.cart.carts(carts)))
  }
}

case class CreateCartForm(product: Int, quantity: Int, client: Int)
case class UpdateCartForm(id: Int, product: Int, quantity: Int, client: Int)