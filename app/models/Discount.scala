package models

import play.api.libs.json.Json

case class Discount(id: Int, discount_size: Int, product: Int)

object Discount {
  implicit val discountFormat = Json.format[Discount]
}
