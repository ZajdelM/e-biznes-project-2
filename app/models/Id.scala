package models

import play.api.libs.json._

case class Id(id: Int)

object Id {
  implicit val idFormat = Json.format[Id]
}
