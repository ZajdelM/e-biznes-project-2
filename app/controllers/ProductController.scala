package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addProduct = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addProductHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateProduct(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateProductHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteProduct(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getProduct(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllProducts = Action {
    Ok(views.html.index("asd"))
  }
}
