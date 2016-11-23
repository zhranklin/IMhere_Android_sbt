package cn.com.forthedream.util

/**
  * Created by Zhranklin on 16/11/22.
  */
object Models {
  case class Place(id: String, name: String)
  case class Item(title: String, `type`: String, content: String, place: String, id: Option[String] = None) {
    def withId(id: String) = Item(title, `type`, content, place, Option(id))
    def eqWithoutId(o: Item) = withId(null) == o.withId(null)
  }
  case class User(username: String, name: String)
  case class UserPass(username: String, name: String, password: String) {
    def asUser = User(username, name)
  }

}
