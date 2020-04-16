package org.sky.service.routes

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes
import org.sky.service.model.{Customer, JsonSupport}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.sky.service.service.DbRegistry

import scala.concurrent.{ExecutionContext, Future}

class SkyCustomerRoutes(val log: LoggingAdapter)(implicit ec: ExecutionContext) extends JsonSupport {

  import StatusCodes._

  lazy val routes: Route =
    concat(
      post {
        path("create") {
          entity(as[Customer]) { customer =>
            complete(
              DbRegistry.customerService.add(customer).map {
                case Some(cust) => Created -> cust
                case None => BadRequest -> null
              })
          }
        }
      },
      (get & path(Segment)) { id =>
        log.info("GET customers/" + id)
        complete {
          val cust: Future[Customer] = DbRegistry.customerService.get(id)
          cust
        }
      },
      get {
        pathEnd {
          log.info("GET customers")
          complete {
            val seqCustomers: Future[Seq[Customer]] = DbRegistry.customerService.get
            seqCustomers
          }
        }
      }
    )
}
