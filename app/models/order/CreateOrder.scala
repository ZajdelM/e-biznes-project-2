package models.order

import play.api.libs.json.Json

case class CreateOrder(client: Int, payment: Int, shipment: Int)

object CreateOrder {
  implicit val createOrderFormat = Json.format[CreateOrder]
}
