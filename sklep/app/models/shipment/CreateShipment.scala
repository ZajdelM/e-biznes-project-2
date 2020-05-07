package models.shipment

import play.api.libs.json.Json

case class CreateShipment(status: String, method: String)

object CreateShipment {
  implicit val createShipmentFormat = Json.format[CreateShipment]
}