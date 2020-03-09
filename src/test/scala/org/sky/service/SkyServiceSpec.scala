package org.sky.service

import akka.event.Logging
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, MessageEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import org.sky.service.model.{Item, Order}
import org.sky.service.routes.{SkyResponses, SkyRoutes}

class SkyServiceSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val log = Logging(system, "SpecialRoutes")
  val skyRoutes = new SkyRoutes(log)

  "The SkyService" should {

    s"get test should return ${SkyResponses.OK}" in {

      // note that there's no need for the host part in the uri:
      val request = Get("/test")
      request ~> skyRoutes.routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // Answer
        entityAs[String] should ===(s"""{"body":"${SkyResponses.OK}"}""")
      }
    }

    "get item without argument must fail" in {

      val request = Get("/item")
      request ~> skyRoutes.routes ~> check {
        handled shouldEqual false
      }
    }

    "post create-order must create an Order" in {

      import spray.json._
      import org.sky.service.model.JsonFormats._

      val orderJson = Order(
        List(
          Item(name = "jordi", id = 1),
          Item(name = "jenni", id = 2)
        )
      ).toJson

      val order = orderJson.prettyPrint

      val request = Post("/create-order", HttpEntity(ContentTypes.`application/json`, order))
      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // Answer
        entityAs[String] should === (s"""{"body":"${SkyResponses.ORDER_CREATED}"}""")
      }
    }
  }
}
