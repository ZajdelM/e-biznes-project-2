package models

import play.api.libs.json.Json

case class Order(id: Int, client: Int, payment: Int, shipment: Int)

object Order {
  implicit val orderFormat = Json.format[Order]
}
