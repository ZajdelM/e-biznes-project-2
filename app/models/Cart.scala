package models

import play.api.libs.json.Json

case class Cart(id: Int, product: Int, quantity: Int, client: Int)

object Cart {
  implicit val cartFormat = Json.format[Cart]
}
