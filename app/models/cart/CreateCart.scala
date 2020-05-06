package models.cart

import play.api.libs.json.Json

case class CreateCart(product: Int, quantity: Int, client: Int)

object CreateCart {
  implicit val createCartFormat = Json.format[CreateCart]
}
