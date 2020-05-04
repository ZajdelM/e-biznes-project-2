package models

import play.api.libs.json.Json

case class Address(id: Int, street: String, building: Int, city: String, postalCode: String)

object Address {
  implicit val addressFormat = Json.format[Address]
}
