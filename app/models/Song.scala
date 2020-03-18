package models

import play.api.libs.json.OFormat

case class Song(
                 name: String,
                 Artist: String,
                 genre : String,
                 feeds: List[Feed])

case class Feed(
                 name: String,
                 url: String)

object JsonFormats {

  import play.api.libs.json.Json

  implicit val feedFormat: OFormat[Feed] = Json.format[Feed]
  implicit val songFormat: OFormat[Song] = Json.format[Song]

}