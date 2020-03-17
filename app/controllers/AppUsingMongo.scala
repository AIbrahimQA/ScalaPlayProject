package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.play.json._
import collection._
import models.{Feed, Song}
import models.JsonFormats._
import play.api.libs.json.{JsValue, Json}
import reactivemongo.api.Cursor

import play.modules.reactivemongo.{
  MongoController, ReactiveMongoComponents, ReactiveMongoApi
}

class AppUsingMongo @Inject()(
                                                    components: ControllerComponents,
                                                    val reactiveMongoApi: ReactiveMongoApi
                                                  ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("persons"))

  def create: Action[AnyContent] = Action.async {
    val song = Song("Gods Plan", "Drake", "Hip Hop", List(Feed("Slashdot news", "http://slashdot.org/slashdot.rdf")))
    val futureResult = collection.flatMap(_.insert.one(song))
    futureResult.map(_ => Ok("User inserted"))
  }

  def createFromJson: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Song].map { song =>
      collection.flatMap(_.insert.one(song)).map { _ => Ok("Song added")
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(artist: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Song]] = collection.map {
      _.find(Json.obj("Artist" -> artist)).
        sort(Json.obj("created" -> -1)).
        cursor[Song]()
    }

    val futureUsersList: Future[List[Song]] =
      cursor.flatMap(
        _.collect[List](
          -1,
          Cursor.FailOnError[List[Song]]()
        )
      )

    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }



}