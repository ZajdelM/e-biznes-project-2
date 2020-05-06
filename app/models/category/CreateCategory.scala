package models.category

import play.api.libs.json._

case class CreateCategory(name: String)

object CreateCategory {
  implicit val createCategoryFormat = Json.format[CreateCategory]
}
