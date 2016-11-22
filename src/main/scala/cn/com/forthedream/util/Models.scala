package cn.com.forthedream.util

/**
  * Created by Zhranklin on 16/11/22.
  */
object Models {
  case class Item(title: String, `type`: String, content: String, place: String, owner: String, id: Option[String] = None)
  case class User(username: String, name: String)
}
