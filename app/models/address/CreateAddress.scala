package models.address

import play.api.libs.json.Json

case class CreateAddress(street: String, building: Int, city: String, postalCode: String)

object CreateAddress {
  implicit val createAddressFormat = Json.format[CreateAddress]
}