package models.payment

import play.api.libs.json.Json

case class CreatePayment(amount: Double, method: String)

object CreatePayment {
  implicit val createPaymentFormat = Json.format[CreatePayment]
}
