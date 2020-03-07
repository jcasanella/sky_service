package org.sky.service

import akka.event.Logging
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, MessageEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import org.sky.service.model.{Item, Order}
import org.sky.service.routes.SkyRoutes

class SkyServiceSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val log = Logging(system, "SpecialRoutes")
  val skyRoutes = new SkyRoutes(log)

  "The SkyService" should {

    "test and empty get (GET /test)" in {

      // note that there's no need for the host part in the uri:
      val request = Get("/test")
      request ~> skyRoutes.routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // Answer
        entityAs[String] should ===("""{"body":"ok"}""")
      }
    }

    "post create-order must create an Order" in {

      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
      import org.sky.service.model.JsonFormats._

      val order =
        """ {
          |   "items": [
          |       { "id": 100, "name": "Test01" }
          |   ]
          | }
          |""".stripMargin

      val request = Post("/create-order", HttpEntity(ContentTypes.`application/json`, order))
      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // Answer
        entityAs[String] should === ("""{"body":"order created"}""")
      }
    }


      //    "post must create an Order" in {
//      val orders = List[Item](
//        Item(name="jordi", id=1),
//        Item(name="jenni", id=2)
//      )
//
//      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
//      import org.sky.service.model.JsonFormats._
//      val ordersEntity = Marshal(orders).to[MessageEntity].futureValue
//
//      // using the RequestBuilding DSL
//      val request = Post("create-order")
//      request ~> skyRoutes.routes ~> check {
//        status should === (StatusCodes.OK)
//      }
//    }
  }
}
