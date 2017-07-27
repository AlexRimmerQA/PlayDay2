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

  def editItem(id: Option[Int]): Action[AnyContent] = Action {
    id match {
      case Some(x) => Ok(views.html.edititem(Item.createItemForm.fill(Item.items.filter(item => item.id == id.get).head)))
      case None => NotFound(<h1>Unable to find this webpage</h1>)
    }
  }

  def editItemData() : Action[AnyContent] = Action { implicit request =>
    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.edititem(formWithErrors))
    },{ item =>
      Item.items.update(Item.items.indexWhere(listItem => listItem.id == item.id), item)
      Redirect(routes.Application.getItems())
    })
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

  def deleteItem(id: Option[Int]) : Action[AnyContent] = Action { implicit request =>
    id match {
      case Some(x) => Ok(views.html.deleteitem(Item.createItemForm.fill(Item.items.filter(item => item.id == x).head)))
      case None => NotFound(<h1>Unable to find this webpage</h1>)
    }
  }

  def deleteItemData() : Action[AnyContent] = Action { implicit request =>
    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.edititem(formWithErrors))
    },{ item =>
      Item.items.remove(Item.items.indexWhere(listItem => listItem.id == item.id))
      Redirect(routes.Application.getItems())
    })
  }
}