package controllers

import javax.inject.{Inject, Singleton}
import models.ClientRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class ClientController  @Inject()(cc: ControllerComponents, clientRepository: ClientRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addClient = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def addClientHandle(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateClient(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def updateClientHandle = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deleteClient(id: Int) = Action {
    Ok(views.html.index("Your new application is ready. Id: " + id))
  }

  def getClient(id: Int) = Action.async { implicit request =>
    clientRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.client(c))
      case None => Redirect(routes.ClientController.getAllClients())
    }
  }

  def getAllClients = Action.async { implicit request =>
    clientRepository.list().map(clients => Ok(views.html.clients(clients)))
  }
}

