package models.product

import play.api.libs.json.Json

case class CreateProduct(name: String, description: String, price: Double, category: Int)

object CreateProduct {
  implicit val createProductFormat = Json.format[CreateProduct]
}
