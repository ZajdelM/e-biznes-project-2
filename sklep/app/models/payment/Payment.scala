package models.payment

import play.api.libs.json.Json

case class Payment(id: Int, amount: Double, method: String)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}
