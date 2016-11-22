package cn.com.forthedream.util
import java.util

import akka.actor.ActorSystem
import cn.com.forthedream.util.Models._
import org.json4s.DefaultFormats
import spray.client.pipelining._
import spray.http._
import spray.httpx.Json4sJacksonSupport

import scala.collection.convert.{DecorateAsJava, DecorateAsScala}
import scala.concurrent.{Await, Future}
import scala.util.Try
import scala.collection.JavaConverters._
import scala.concurrent.duration._

object JsonSupport extends Json4sJacksonSupport {
  implicit def json4sJacksonFormats = DefaultFormats
}

object Client {
  import JsonSupport._

  implicit val system = ActorSystem()
  import system.dispatcher

  val pipeline: HttpRequest ⇒ Future[HttpResponse] = (
      addCredentials(BasicHttpCredentials("sht", "pass"))
      ~> sendReceive)
  val response: Future[HttpResponse] =
    pipeline(Get("http://zhranklin.com/imh/user"))

  val userTest = Await.result(response, 10 second).entity.asString
}

trait Util extends DecorateAsJava with DecorateAsScala {
  import java.net.{URLDecoder, URLEncoder}
  val encode = URLEncoder.encode(_: String, "utf-8")
  val decode = URLDecoder.decode(_: String, "utf-8")
  trait _idRename
}

object Util extends Util

