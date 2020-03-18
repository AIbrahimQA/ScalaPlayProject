package models

import play.api.data.Form
import play.api.data.Forms._

case class SongInfo(name: String, Artist: String, genre: String){

  override def toString() = s"The name of the Song is: $name and the Artist is: $Artist and the Genre is: $genre."

}

object SongInfo {
  val postSong = Form(
    mapping(
      "name" -> nonEmptyText,
      "Artist" -> nonEmptyText,
      "genre" -> nonEmptyText
    )(SongInfo.apply)(SongInfo.unapply)
  )
}