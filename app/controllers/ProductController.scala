package controllers

import javax.inject.{Inject, Singleton}
import models.category.{Category, CategoryRepository}
import models.product.{CreateProduct, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class ProductController @Inject()(cc: MessagesControllerComponents, productRepository: ProductRepository, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createProductForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> of(doubleFormat),
      "category" -> number,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> of(doubleFormat),
      "category" -> number,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def getAllProductsJson(): Action[AnyContent] = Action.async {
    productRepository.list()
      .map(products => Json.toJson(products))
      .map(jsonProducts => Ok(jsonProducts))
  }

  def getProductsByIdJson(id: Int): Action[AnyContent] = Action.async {
    productRepository.getByIdOption(id)
      .map {
        case Some(p) => Ok(Json.toJson(p))
        case None => NotFound
      }
  }

  def addProductJson: Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val createProductJson = json.as[CreateProduct]

    categoryRepository.getById(createProductJson.category).flatMap(category =>
      productRepository.create(createProductJson.name, createProductJson.description, createProductJson.price, category.id).map(
        createdProduct => Created(Json.toJson(createdProduct))
      )
    )
  }

  def updateProductJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val updateProductJson = json.as[Product]

    categoryRepository.getById(updateProductJson.category).flatMap(_ =>
      productRepository.getById(id).flatMap(product =>
        productRepository.update(product.id, updateProductJson).map(
          updatedProduct => Ok(Json.toJson(updatedProduct))
        )
      )
    )
  }

  def deleteProductJson(id: Int): Action[AnyContent] = Action.async {
    productRepository.delete(id)
    Future(NoContent)
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepository.list()
    categories.map(cat => Ok(views.html.product.productAdd(createProductForm, cat)))
  }

  def addProductHandle = Action.async { implicit request =>
    var categ: Seq[Category] = Seq[Category]()
    val categories = categoryRepository.list().onComplete {
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    createProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.product.productAdd(errorForm, categ))
        )
      },
      product => {
        productRepository.create(product.name, product.description, product.price, product.category).map { _ =>
          Redirect(routes.ProductController.addProduct()).flashing("success" -> "product.created")
        }
      }
    )
  }

  def updateProduct(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var categories: Seq[Category] = Seq[Category]()
    categoryRepository.list().onComplete {
      case Success(cat) => categories = cat
      case Failure(_) => print("fail")
    }

    val product = productRepository.getById(id)
    product.map(prod => {
      val prodForm = updateProductForm.fill(UpdateProductForm(prod.id, prod.name, prod.description, prod.price, prod.category))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.product.productUpdate(prodForm, categories))
    })
  }

  def updateProductHandle = Action.async { implicit request =>
    var categ: Seq[Category] = Seq[Category]()
    val categories = categoryRepository.list().onComplete {
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    updateProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.product.productUpdate(errorForm, categ))
        )
      },
      product => {
        productRepository.update(product.id, Product(product.id, product.name, product.description, product.price, product.category)).map { _ =>
          Redirect(routes.ProductController.updateProduct(product.id)).flashing("success" -> "product updated")
        }
      }
    )

  }

  def deleteProduct(id: Int) = Action {
    productRepository.delete(id)
    Redirect(routes.ProductController.getAllProducts())
  }

  def getProduct(id: Int) = Action.async { implicit request =>
    productRepository.getByIdOption(id).map {
      case Some(p) => Ok(views.html.product.product(p))
      case None => Redirect(routes.ProductController.getAllProducts())
    }
  }

  def getAllProducts = Action.async { implicit request =>
    productRepository.list().map(prod => Ok(views.html.product.products(prod)))
  }
}

case class CreateProductForm(name: String, description: String, price: Double, category: Int)

case class UpdateProductForm(id: Int, name: String, description: String, price: Double, category: Int)