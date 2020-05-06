package controllers

import javax.inject.{Inject, Singleton}
import models.{Address, AddressRepository, Client, ClientRepository}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ClientController  @Inject()(cc: MessagesControllerComponents, clientRepository: ClientRepository, addressRepository: AddressRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){
  // TODO:  dokonczyc clienta i cart analogicznie do pozostalych, przetestowac czy wszystko dziala i potem jsony zrobic 
  val createClientForm: Form[CreateClientForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "surname" -> nonEmptyText,
      "address" -> number
    )(CreateClientForm.apply)(CreateClientForm.unapply)
  }

  val updateClientForm: Form[UpdateClientForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "surname" -> nonEmptyText,
      "address" -> number
    )(UpdateClientForm.apply)(UpdateClientForm.unapply)
  }

  def addClient = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val addresses = addressRepository.list()
    addresses.map(address => Ok(views.html.client.clientAdd(createClientForm, address)))
  }

  def addClientHandle = Action.async { implicit request =>
    var addre:Seq[Address] = Seq[Address]()
    val addresses = addressRepository.list().onComplete{
      case Success(addr) => addre = addr
      case Failure(_) => print("fail")
    }

    createClientForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.client.clientAdd(errorForm, addre))
        )
      },
      client => {
        clientRepository.create(client.name, client.surname, client.address).map { _ =>
          Redirect(routes.ClientController.addClient()).flashing("success" -> "client created")
        }
      }
    )
  }

  def updateClient(id: Int) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var addresses: Seq[Address] = Seq[Address]()
    addressRepository.list().onComplete {
      case Success(addr) => addresses = addr
      case Failure(_) => print("fail")
    }

    val client = clientRepository.getById(id)
    client.map(cli => {
      val clientForm = updateClientForm.fill(UpdateClientForm(cli.id, cli.name, cli.surname, cli.address))
      //  id, product.name, product.description, product.category)
      //updateProductForm.fill(prodForm)
      Ok(views.html.client.clientUpdate(clientForm, addresses))
    })
  }

  def updateClientHandle = Action.async { implicit request =>
    var addr:Seq[Address] = Seq[Address]()
    val addresses = addressRepository.list().onComplete{
      case Success(address) => addr = address
      case Failure(_) => print("fail")
    }

    updateClientForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.client.clientUpdate(errorForm, addr))
        )
      },
      client => {
        clientRepository.update(client.id, Client(client.id, client.name, client.surname, client.address)).map { _ =>
          Redirect(routes.ClientController.updateClient(client.id)).flashing("success" -> "client updated")
        }
      }
    )
  }

  def deleteClient(id: Int) = Action {
    clientRepository.delete(id)
    Redirect(routes.ClientController.getAllClients())
  }

  def getClient(id: Int) = Action.async { implicit request =>
    clientRepository.getByIdOption(id).map {
      case Some(c) => Ok(views.html.client.client(c))
      case None => Redirect(routes.ClientController.getAllClients())
    }
  }

  def getAllClients = Action.async { implicit request =>
    clientRepository.list().map(clients => Ok(views.html.client.clients(clients)))
  }

}

case class CreateClientForm(name: String, surname: String, address: Int)
case class UpdateClientForm(id: Int, name: String, surname: String, address: Int)