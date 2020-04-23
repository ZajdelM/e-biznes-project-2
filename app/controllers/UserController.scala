package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UserController  @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def addUser = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addUserHandle(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateUser(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateUserHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteUser(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getUser(id: Long) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getAllUsers = Action {
    Ok(views.html.index("asd"))
  }
}

