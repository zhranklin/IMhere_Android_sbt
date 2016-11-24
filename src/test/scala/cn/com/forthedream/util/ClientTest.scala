package cn.com.forthedream.util

import java.util

import cn.com.forthedream.util.Client.PUBLIC
import cn.com.forthedream.util.Models._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._

import scala.collection.JavaConverters._


class ClientTest extends FlatSpec with GivenWhenThen with ScalaFutures with Matchers {
  behavior of "Client"
  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  it should "sendReq" in {
    val client = new Client("sht", "pass")
    val (code, result) = client.connect("http://zhranklin.com/imh/user", "GET")
    assert(code == 200, "code should be 200")
    assert(result.contains("{") && result.contains("}"), "the result should be a JSON object")
  }

  behavior of "user dao"
  Given("a userpass")
  val userpass = UserPass("zrkn-user-test", "test-name", "test-pass")
  it should "create new user" in {
    When("register")
    whenReady(PUBLIC.addUser(userpass)) { userResp ⇒
      assert(userResp == (200, userpass.asUser))
    }
  }

  behavior of "client with credentials"
  Given("a client for test")
  val client = new Client(userpass.username, userpass.password)
  val user = userpass.asUser

  val mockItems = List(
    Item("zrkn-test-title0", "text", "kkk", "001"),
    Item("zrkn-test-title1", "html", "<h1>head</h1><p>ttext</p>", "001"),
    Item("zrkn-test-title2", "html", "<h2>head2</h2><p>text</p>", "002"),
    Item("zrkn-test-title3", "url", "http://www.baidu.com", "002"),
    Item("zrkn-test-title4", "text", "kkk4", "001"),
    Item("zrkn-test-title5", "text", "kkk5", "002"),
    Item("zrkn-test-title6", "text", "kkk6", "001"),
    Item("zrkn-test-title7", "text", "kkk7", "002"),
    Item("zrkn-test-title8", "text", "kkk8", "001"),
    Item("zrkn-test-title9", "text", "kkk9", "002"))

  it should "addItem" in {
    val futures = mockItems.map(i ⇒ (i, client.addItem(i)))
    futures.foreach { iFuture ⇒
      When("adding it")
      val item = iFuture._1
      val resp = iFuture._2.futureValue
      Then("it should resturn right thing")
      assert(resp._1 == 200)
      val respItem = resp._2
      val id = respItem.id.get
      assert(id matches "[a-fA-F0-9]{24}", s"id is $id")
      assert(respItem == item.withId(id), s"$respItem != ${item.withId(id)}  ")
    }
  }


  var itemsWithId: List[Item] = _
  it should "getItems" in {
    When("list items of the users")
    val (itemsCode, itemsResp) = client.getItems.futureValue
    itemsWithId = itemsResp.asScala.to[List]
    Then("responded items should be right")
    assert(itemsCode == 200)
    itemsWithId.map(_.withId(null)) should contain allElementsOf mockItems
  }

  it should "delItem" in {
    When("deleting a user")
    val id = itemsWithId(3).id.get
    val resp = client.delItem(id).futureValue
    Then("responded item should be right")
    assert(resp._1 == 200)
    itemsWithId should contain (resp._2)
  }

  it should "putItem" in {
    When("puting a user")
    val nType = "put type"
    val item = itemsWithId(1)
    val toPut = Item(item.title, nType, item.content, item.place, item.id)

    val resp = client.putItem(toPut.id.get, toPut).futureValue
    assert(resp._1 == 200)
    itemsWithId should contain (toPut)
  }

  val mockPlaces = List(
    Place("001", "pl1"),
    Place("002", "pl2"),
    Place("003", "pl3"),
    Place("004", "pl4"),
    Place("005", "pl5"),
    Place("006", "pl6"))

  it should "addPlace" in {
    val futures = mockPlaces zip mockPlaces.map(client.addPlace)
    futures.foreach { iFuture ⇒
      When("adding it")
      val place = iFuture._1
      val resp = iFuture._2.futureValue
      Then("it should resturn right thing")
      assert(resp == (200, place))
    }
  }

  it should "putPlace" in {
    val toPut = Place("003", "new pl")
    val resp = client.putPlace(toPut.id, toPut).futureValue
    assert(resp == (200, toPut))
  }

  it should "delPlace" in {
    val toDel = mockPlaces(4)
    var resp = client.delPlace(toDel.id).futureValue
    assert(resp == (200, toDel))
    assertThrows[Exception](client.getPlace(toDel.id).futureValue)
  }
}
