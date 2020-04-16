package org.sky.service

import java.util.{Calendar, Date}

import akka.event.Logging
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import org.sky.service.model.{Customer, JsonSupport}
import org.sky.service.routes.SkyRoutes

class SkyServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport with ScalaFutures
  with SkyServiceFixtures {

  val log = Logging(system, "SkyRoutes")
  val routeStr = "/v1/customers"
  val skyRoutes = new SkyRoutes(log)

  def buildDate(year: Int, month: Int, day: Int): Date = {

    val cal = Calendar.getInstance
    cal.set(year, month, day)
    cal.getTime
  }

  "The SkyService Customers" should {

    s"return no customers if no present (GET ${routeStr}" in {

      val request = Get(routeStr)

      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.OK)

        contentType should === (ContentTypes.`application/json`)

      entityAs[String] should === ("[]")
      }
    }

    s"be able to add Customers (POST ${routeStr}/create" in {

      val cust = Customer(personalId = "1", name = "George", surname = "Smith", dob = buildDate(1979, 11, 29),
        address = "124 London Road", zipcode = "MK12", city = "London")
      val customerEntity = Marshal(cust).to[MessageEntity].futureValue

      val request = Post(s"${routeStr}/create").withEntity(customerEntity)

      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.Created)

        contentType should === (ContentTypes.`application/json`)

        entityAs[String] should === (customer1)
      }

      val cust2 = Customer(personalId = "2", name = "Sophie", surname = "Larrou", dob = buildDate(1979, 10, 29),
        address = "421 Manchester Road", zipcode = "WO12", city = "Wolverton")

      val customerEntity2 = Marshal(cust2).to[MessageEntity].futureValue

      val request2 = Post(s"${routeStr}/create").withEntity(customerEntity2)

      request2 ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.Created)

        contentType should === (ContentTypes.`application/json`)

        entityAs[String] should === (customer2)
      }
    }

    s"return customers (GET ${routeStr}" in {

      val request = Get(routeStr)

      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.OK)

        contentType should === (ContentTypes.`application/json`)

        entityAs[String] should === (s"[${customers.mkString(",")}]")
      }
    }

    s"return customer by id (GET ${routeStr}/${custId1}" in {

      val request = Get(routeStr + "/" + custId1)

      request ~> skyRoutes.routes ~> check {
        status should === (StatusCodes.OK)

        contentType should === (ContentTypes.`application/json`)

        entityAs[String] should === (customer1)
      }
    }
  }
}
