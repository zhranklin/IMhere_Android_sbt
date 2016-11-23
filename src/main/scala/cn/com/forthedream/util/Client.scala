package cn.com.forthedream.util

import java.io._
import java.net.{HttpURLConnection, URL}
import java.nio.charset.Charset
import java.util

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.io.Source
import scala.util._

class NetworkException(msg: String, val responseCode: Int, cause: Throwable) extends Exception {
  override def getMessage: String = msg
  initCause(cause)
}

class Client(user: String, pass: String) {
  import scala.concurrent.ExecutionContext.Implicits._

  protected def addCredentials(urlConnection: HttpURLConnection) = {
    var basicAuth = "Basic " + new String(Base64.encodeToString(s"$user:$pass".getBytes()))
    urlConnection.setRequestProperty("Authorization", basicAuth)
  }

  def connect(urlStr: String, method: String, content: String = null): (Int, String) = {
    val url = new URL(urlStr)
    val urlConnection = url.openConnection().asInstanceOf[HttpURLConnection]

    addCredentials(urlConnection)
    urlConnection.setRequestMethod(method)
    urlConnection.setConnectTimeout(5000)
    urlConnection.setReadTimeout(2000000)

    if (Set("POST", "PUT") contains method) {
      assert(content != null, "POST, PUT方法必须包含上传内容")
      urlConnection.setRequestProperty("Content-Type", "application/json")
      urlConnection.setDoOutput(true)
      urlConnection.setDoInput(true)
      val writer = new OutputStreamWriter(urlConnection.getOutputStream, Charset.forName("utf-8"))
      writer.append(content)
      writer.close()
    }
    val code = urlConnection.getResponseCode
    val msg = try Source.fromInputStream(urlConnection.getInputStream()).mkString.toString catch {
      case e: Exception ⇒ throw new NetworkException("Error reading response", code, e)
    }
    (code, msg)
  }

  def cntFuture(url: String, method: String, content: String = null) = Future(connect(url, method, content))
  def getModel[T: Manifest](url: String, method: String, content: String = null): Future[(Int, T)] =
    Future(connect(url, method, content)).map(unmarshal[T])

  val urlBase = "http://zhranklin.com/imh"
  val (get, post, put, delete) = ("GET", "POST", "PUT", "DELETE")

  import org.json4s._
  import jackson.Serialization._

  implicit val formats = DefaultFormats

  private def unmarshal[T: Manifest](from: (Int, String)): (Int, T) = (from._1,
    try read[T](from._2) catch {
      case e: Exception ⇒ throw new NetworkException("cannot transform to object", from._1, e)
    }
  )

  import Models._

  val userUrl = s"$urlBase/user"
  def getUser: Future[(Int, User)] = getModel[User](userUrl, get)
  def addUser(user: UserPass): Future[(Int, User)] = getModel[User](userUrl, post, write(user))
  //  def delUser = cntFuture(userUrl, delete).map(unmarshal[User])

  val placeUrl = s"$urlBase/place"
  def getPlace(id: String): Future[(Int, Place)] = getModel[Place](s"$placeUrl/$id", get)
  def delPlace(id: String): Future[(Int, Place)] = getModel[Place](s"$placeUrl/$id", delete)
  def putPlace(id: String, pl: Place): Future[(Int, Place)] = getModel[Place](s"$placeUrl/$id", put, write(pl))
  def addPlace(pl: Place): Future[(Int, Place)] = getModel[Place](placeUrl, post, write(pl))

  val itemUrl = s"$urlBase/item"
  def getItems: Future[(Int, util.List[Item])] =
    getModel[List[Item]](s"$itemUrl/", get).map(tp ⇒ (tp._1, tp._2.asJava))
  def getItem(id: String): Future[(Int, Item)] = getModel[Item](s"$itemUrl/$id", get)
  def delItem(id: String): Future[(Int, Item)] = getModel[Item](s"$itemUrl/$id", delete)
  def putItem(id: String, item: Item): Future[(Int, Item)] = getModel[Item](s"$itemUrl/$id", put, write(setItemOwner(item)))
  def addItem(item: Item): Future[(Int, Item)] = getModel[Item](s"$itemUrl", post, write(setItemOwner(item)))

  private def setItemOwner(i: Item) = new Item(i.title, i.`type`, i.content, i.place, i.id)

}

object Client {
  import scala.concurrent.ExecutionContext.Implicits._

  object PUBLIC extends Client(null, null) {
    override protected def addCredentials(urlConnection: HttpURLConnection) = {}
  }

  trait Handler[T] {
    def succ(t: T)
    def fail(e: Throwable)
  }

  def handleFuture[T](future: Future[T], handler: Handler[T]) = future.onComplete {
    case Success(obj) ⇒ handler.succ(obj)
    case Failure(t) ⇒ handler.fail(t)
  }

}
