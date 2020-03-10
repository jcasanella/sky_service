package org.sky.service.routes

import org.sky.service.db.ActionRepository
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import org.sky.service.model.{Order, Order2}

import scala.concurrent.duration._
import scala.concurrent.Future

class SkyRoutes2(actionRepository: ActorRef[ActionRepository.Command])(implicit system: ActorSystem[_])
  extends SkyResponsesJson {

  implicit val timeout: Timeout = 3.seconds
//  implicit val scheduler = system.scheduler

  import akka.actor.typed.scaladsl.AskPattern._

  lazy val routes2: Route =
    concat(
      get {
        pathPrefix("item" / LongNumber ) { id =>
          val maybeJob: Future[Option[Order2]] = actionRepository.ask(ActionRepository.getItem(id, _))
          rejectEmptyResponse {
            complete("maybeJob")
          }
        }
      },
      post {
        path("create-order") {
          entity(as[Order2]) { order =>
            val operationPerformed: Future[SkyResponses.Response] = actionRepository.ask(ActionRepository.addOrder(order, _))
            onSuccess(operationPerformed) {
              case SkyResponses.OK2 => complete("Added")
              case SkyResponses.KO(reason) => complete(StatusCodes.InternalServerError -> reason)
            }
          }
        }
      }
    )
}
