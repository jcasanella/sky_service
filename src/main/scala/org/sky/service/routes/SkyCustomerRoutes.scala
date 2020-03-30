package org.sky.service.routes

import akka.http.scaladsl.model.StatusCodes
import org.sky.service.model.{Customer, JsonSupport}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.sky.service.service.CustomerService

import scala.concurrent.ExecutionContext

class SkyCustomerRoutes(implicit ec: ExecutionContext) extends JsonSupport {

  import StatusCodes._

  lazy val routes: Route =
    concat(
      post {
        path("create") {
          entity(as[Customer]) { customer =>
            complete(CustomerService.addCustomer(customer).map {
              case Some(cust) => Created -> cust
              case None => BadRequest -> null
            })
          }
        }
      }
    )
}
