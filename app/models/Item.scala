package models

import play.api.data.Form
import play.api.data.Forms._

import scala.collection.mutable.ArrayBuffer

case class Item (
                  id: Int,
                  name: String,
                  description: String,
                  maker: String,
                  warrentyTime: String,
                  price: BigDecimal,
                  discount: Int,
                  seller: String
                )

object Item {
  val createItemForm = Form(
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "maker" -> nonEmptyText,
      "warrentyTime" -> nonEmptyText,
      "price" -> bigDecimal(10,2),
      "discount" -> number,
      "seller" -> nonEmptyText
    )(Item.apply)(Item.unapply)
  )

  val items = ArrayBuffer(
    Item(1, "Item1", "This is item 1", "Maker1", "22/03/2021", 11.11, 21, "Seller1"),
    Item(2, "Item2", "This is item 2", "Maker2", "22/03/2022", 22.22, 22, "Seller2"),
    Item(3, "Item3", "This is item 3", "Maker3", "22/03/2023", 33.33, 23, "Seller3"),
    Item(4, "Item4", "This is item 4", "Maker4", "22/03/2024", 44.44, 24, "Seller4"),
    Item(5, "Item5", "This is item 5", "Maker5", "22/03/2025", 55.55, 25, "Seller5"),
    Item(6, "Item6", "This is item 6", "Maker6", "22/03/2026", 66.66, 26, "Seller6")
  )
}
