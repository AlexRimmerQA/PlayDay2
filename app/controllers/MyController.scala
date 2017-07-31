package controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._

class MyController @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {
  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("persons"))

  def create: Action[AnyContent] = Action.async {
    val user = User(29, "FirstName", "Lastname", List(Feed("BBC news", "http://www.bbc.co.uk")))
    val futureResult = collection.flatMap(_.insert(user))
    futureResult.map(_ => Ok("Added user " + user.firstName + " " + user.lastName))
  }

  def findByName: Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[User]] = collection.map {
      _.find(Json.obj("lastName" -> "LastNaem"))
        .sort(Json.obj("created" -> -1))
        .cursor[User]
    }
    val futureUsersList: Future[List[User]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { persons =>
      Ok(persons.head.toString)
    }
  }

  def update : Action[AnyContent] = Action.async { implicit request =>
    val user = User(29, "FirstName", "LastNaem", List(Feed("BBC news", "http://www.bbc.co.uk")))
    val modifier = Json.obj("$set" -> Json.obj("lastName" -> user.lastName))
    val futureResult = collection.flatMap(_.update(Json.obj("lastName" -> "Lastname"), modifier))
    futureResult.map { _ =>
      Redirect(routes.MyController.findByName())
    }
  }

  def remove : Action[AnyContent] = Action.async { implicit request =>
    val user = User(29, "FirstName", "LastNaem", List(Feed("BBC news", "http://www.bbc.co.uk")))
    val modifier = Json.obj("$set" -> Json.obj("lastName" -> user.lastName))
    val futureResult = collection.flatMap(_.remove(Json.obj("lastName" -> "LastNaem")))
    futureResult.map { _ =>
      Redirect(routes.MyController.findByName())
    }
  }
}
