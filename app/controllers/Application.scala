package controllers

import javax.inject.Inject

import models.Item
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getItems: Action[AnyContent] =  Action {
    Ok(views.html.getitems(Item.items))
  }

  def editItem(name: Option[String]): Action[AnyContent] = Action {
    name match {
      case Some(x) => Ok(views.html.edititem(Item.items.filter(item => item.name == name.get).head))
      case None => NotFound(<h1>Unable to find this webpage</h1>)
    }
  }

  def addItem() : Action[AnyContent] = Action { implicit request =>
    Ok(views.html.additem(Item.createItemForm))
  }

  def createItem() : Action[AnyContent] = Action { implicit request =>
    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.additem(formWithErrors))
    },{ item =>
      Item.items.append(item)
      Redirect(routes.Application.getItems())
    })
  }
}