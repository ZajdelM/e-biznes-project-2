package models

import play.api.libs.json.Json

case class Shipment(id: Int, status: String, method: String)

object Shipment {
  implicit val shipmentFormat = Json.format[Shipment]
}