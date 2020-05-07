package controllers

import javax.inject.{Inject, Singleton}
import models.category.{Category, CategoryRepository, CreateCategory}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(cc: MessagesControllerComponents, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){

  val createCategoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def getAllCategoriesJson(): Action[AnyContent] = Action.async {
    categoryRepository.list()
      .map(categories => Json.toJson(categories))
      .map(jsonCategories => Ok(jsonCategories))
  }

  def getCategoryByIdJson(id: Int): Action[AnyContent] = Action.async {
    categoryRepository.getByIdOption(id)
      .map {
        case Some(c) => Ok(Json.toJson(c))
        case None => NotFound
      }
  }

  def addCategoryJson: Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val createCategoryJson = json.as[CreateCategory]

    categoryRepository.create(createCategoryJson.name).map(
      createdCategory => Created(Json.toJson(createdCategory))
    )
  }

  def updateCategoryJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val json = request.body.asJson.get
    val updateCategoryJson = json.as[Category]

    categoryRepository.getById(id).flatMap(existingCategory =>
      categoryRepository.update(existingCategory.id, updateCategoryJson).map(
        updatedCategory => Ok(Json.toJson(updatedCategory))
      )
    )
  }

  def deleteCategoryJson(id: Int): Action[AnyContent] = Action.async {
    categoryRepository.delete(id)
    Future(NoContent)
  }

  def addCategory = Action.async { implicit request =>
    Future.successful(Ok(views.html.category.categoryAdd(createCategoryForm)))
  }

  def addCategoryHandle = Action.async { implicit request =>
    createCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.category.categoryAdd(errorForm))
        )
      },
      cat => {
        categoryRepository.create(cat.name).map { _ =>
          Redirect(routes.CategoryController.addCategory()).flashing("success" -> "category created")
        }
      }
    )
  }

  def updateCategory(id: Int) = Action.async { implicit request =>
    val category = categoryRepository.getById(id)
    category.map(cat => {
      val catForm = updateCategoryForm.fill(UpdateCategoryForm(cat.id, cat.name))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.category.categoryUpdate(catForm))
    })
  }

  def updateCategoryHandle = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.category.categoryUpdate(errorForm))
        )
      },
      cat => {
        categoryRepository.update(cat.id, Category(cat.id, cat.name)).map { _ =>
          Redirect(routes.CategoryController.updateCategory(cat.id)).flashing("success" -> "category updated")
        }
      }
    )
  }

  def deleteCategory(id: Int) = Action {
    categoryRepository.delete(id)
    Redirect(routes.CategoryController.getAllCategories())
  }

  def getCategory(id: Int) = Action.async { implicit request =>
    categoryRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.category.category(c))
      case None => Redirect(routes.AddressController.getAllAddresses())
    }
  }

  def getAllCategories = Action.async { implicit request =>
    categoryRepository.list().map(cate => Ok(views.html.category.categories(cate)))
  }

}

case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(id: Int, name: String)