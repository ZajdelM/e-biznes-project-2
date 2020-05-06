package models.discount

import play.api.libs.json.Json

case class CreateDiscount(discountSize: Int, product: Int)

object CreateDiscount {
  implicit val createDiscountFormat = Json.format[CreateDiscount]
}
