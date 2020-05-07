package models.news

import play.api.libs.json._

case class CreateNews(message: String)

object CreateNews {
  implicit val createNewsFormat = Json.format[CreateNews]
}
