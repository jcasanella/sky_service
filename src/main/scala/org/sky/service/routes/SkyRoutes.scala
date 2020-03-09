package org.sky.service.routes

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, onComplete, onSuccess, path, pathPrefix, post}
import org.sky.service.model.{Message, Order, Order2}

import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import org.sky.service.db.{ActionRepository, Db}
import org.sky.service.model.JsonFormats._

class SkyRoutes(val log: LoggingAdapter)(implicit ec: ExecutionContextExecutor) {

  val db = new Db

  val routes =
    concat(
      get {
        pathPrefix("item" / LongNumber ) { id =>
          val maybeItem = db.fetchItem(id)
          onSuccess(maybeItem) {
            case Some(item) => {
              log.info(s"Found item with id: ${item.id} Name: ${item.name}")
              complete(item)
            }
            case None => {
              log.info(s"No found item with id ${id}")
              complete(StatusCodes.NotFound)
            }
          }
        }
      },
      get {
        path("test") {
          complete(Message(SkyResponses.OK))
        }
      },
      post {
        path("create-order") {
          entity(as[Order]) { order =>
            val saved = db.saveOrder(order)
            onComplete(saved) { done =>
              log.info(s"Order created.")
              complete(Message(SkyResponses.ORDER_CREATED))
            }
          }
        }
      }
    )
}
