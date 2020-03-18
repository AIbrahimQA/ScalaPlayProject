package controllers
import javax.inject.{Inject, Singleton}
import javax.inject._
import models.SongInfo
import play.api.mvc._


@Singleton
class SongController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def showSongForm() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.submitSong(SongInfo.postSong))
  }

  def submitSong = Action { implicit request: Request[AnyContent] =>
    SongInfo.postSong.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.submitSong(formWithErrors))
    }, { song =>
      Ok(song.toString())
    })
  }

}