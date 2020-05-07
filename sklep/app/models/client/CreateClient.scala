package models.client

import play.api.libs.json.Json

case class CreateClient(name: String, surname: String, address: Int)

object CreateClient {
  implicit val createClientFormat = Json.format[CreateClient]
}
