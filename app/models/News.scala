package models

import play.api.libs.json._

case class News(id: Int, message: String)

object News {
  implicit val newsFormat = Json.format[News]
}
