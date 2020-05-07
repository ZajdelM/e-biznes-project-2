package controllers

import javax.inject.{Inject, Singleton}
import models.discount.{CreateDiscount, Discount, DiscountRepository}
import models.product
import models.product.ProductRepository
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class DiscountController @Inject()(cc: MessagesControllerComponents, discountRepository: DiscountRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  val createDiscountForm: Form[CreateDiscountForm] = Form {
    mapping(
      "discountSize" -> number(min = 0, max = 100),
      "product" -> number
    )(CreateDiscountForm.apply)(CreateDiscountForm.unapply)
  }

  val updateDiscountForm: Form[UpdateDiscountForm] = Form {
    mapping(
      "id" -> number,
      "discountSize" -> number(min = 0, max = 100),
      "product" -> number
    )(UpdateDiscountForm.apply)(UpdateDiscountForm.unapply)
  }

  def getAllDiscountsJson(): Action[AnyContent] = Action.async {
    discountRepository.list()
      .map(discounts => Json.toJson(discounts))
      .map(jsonDiscounts => Ok(jsonDiscounts))
  }

  def getDiscountByIdJson(id: Int): Action[AnyContent] = Action.async {
    discountRepository.getByIdOption(id)
      .map {
        case Some(d) => Ok(Json.toJson(d))
        case None => NotFound
      }
  }

  def addDiscountJson: Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val createDiscountJson = json.as[CreateDiscount]

    productRepository.getById(createDiscountJson.product).flatMap(product =>
      discountRepository.create(createDiscountJson.discountSize, product.id).map(
        createdDiscount => Created(Json.toJson(createdDiscount))
      )
    )
  }


  def updateDiscountJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val updateDiscountJson = json.as[Discount]

    productRepository.getById(updateDiscountJson.product).flatMap(_ =>
      discountRepository.getById(id).flatMap(client =>
        discountRepository.update(client.id, updateDiscountJson).map(
          updatedDiscount => Ok(Json.toJson(updatedDiscount))
        )
      )
    )
  }

  def deleteDiscountJson(id: Int): Action[AnyContent] = Action.async {
    discountRepository.delete(id)
    Future(NoContent)
  }

  def addDiscount = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepository.list()
    products.map(prod => Ok(views.html.discount.discountAdd(createDiscountForm, prod)))
  }

  def addDiscountHandle  = Action.async { implicit request =>
    var prods:Seq[product.Product] = Seq[product.Product]()
    val products = productRepository.list().onComplete{
      case Success(prod) => prods = prod
      case Failure(_) => print("fail")
    }

    createDiscountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.discount.discountAdd(errorForm, prods))
        )
      },
      discount => {
        discountRepository.create(discount.discount_size, discount.product).map { _ =>
          Redirect(routes.DiscountController.addDiscount()).flashing("success" -> "discount created")
        }
      }
    )
  }

  def updateDiscount(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var products: Seq[product.Product] = Seq[product.Product]()
    productRepository.list().onComplete {
      case Success(prod) => products = prod
      case Failure(_) => print("fail")
    }

    val discount = discountRepository.getById(id)
    discount.map(disc => {
      val prodForm = updateDiscountForm.fill(UpdateDiscountForm(disc.id, disc.discountSize, disc.product))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.discount.discountUpdate(prodForm, products))
    })
  }

  def updateDiscountHandle = Action.async { implicit request =>
    var prod:Seq[product.Product] = Seq[product.Product]()
    val products = productRepository.list().onComplete{
      case Success(prods) => prod = prods
      case Failure(_) => print("fail")
    }

    updateDiscountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.discount.discountUpdate(errorForm, prod))
        )
      },
      discount => {
        discountRepository.update(discount.id, Discount(discount.id, discount.discountSize, discount.product)).map { _ =>
          Redirect(routes.DiscountController.updateDiscount(discount.id)).flashing("success" -> "discount updated")
        }
      }
    )
  }

  def deleteDiscount(id: Int) = Action {
    discountRepository.delete(id)
    Redirect(routes.DiscountController.getAllDiscounts())
  }

  def getDiscount(id: Int) = Action.async { implicit request =>
    discountRepository.getByIdOption(id).map {
        case Some(n) => Ok(views.html.discount.discount(n))
        case None => Redirect(routes.DiscountController.getAllDiscounts())
      }
  }

  def getAllDiscounts = Action.async { implicit request =>
    discountRepository.list().map(discounts => Ok(views.html.discount.discounts(discounts)))
  }

}

case class CreateDiscountForm(discount_size: Int, product: Int)
case class UpdateDiscountForm(id: Int, discountSize: Int, product: Int)