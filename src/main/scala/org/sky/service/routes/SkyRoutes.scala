package org.sky.service.routes

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

class SkyRoutes(val log: LoggingAdapter)(implicit ec: ExecutionContext) {

  implicit val timeout: Timeout = 3.seconds

  private val skyCustomerRoutes = new SkyCustomerRoutes()

  lazy val routes: Route =
    pathPrefix("v1") {
      pathPrefix("customers") {
        skyCustomerRoutes.routes
      }
    }
}
