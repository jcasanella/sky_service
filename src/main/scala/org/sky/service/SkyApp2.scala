package org.sky.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, PostStop}
import akka.actor.typed.scaladsl.adapter._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.Http
import org.sky.service.db.ActionRepository
import org.sky.service.routes.SkyRoutes2

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object SkyApp2 {

  sealed trait Message
  private final case class StartFailed(cause: Throwable) extends Message
  private final case class Started(binding: ServerBinding) extends Message
  case object Stop extends Message

  def apply(host: String, port: Int): Behavior[Message] = Behaviors.setup { ctx =>

    implicit val system = ctx.system
    implicit val untypedSystem: akka.actor.ActorSystem = ctx.system.toClassic

    implicit val materializer: ActorMaterializer = ActorMaterializer()(ctx.system.toClassic)
    implicit val ec: ExecutionContextExecutor = ctx.system.executionContext

    val actionRepository = ctx.spawn(ActionRepository(), "ActionRepository")
    val routes = new SkyRoutes2(actionRepository)

    val serverBinding: Future[Http.ServerBinding] = Http.apply().bindAndHandle(routes.routes2, host, port)
    ctx.pipeToSelf(serverBinding) {
      case Success(binding) => Started(binding)
      case Failure(exception) => StartFailed(exception)
    }

    def running(binding: ServerBinding): Behavior[Message] = {
      Behaviors.receiveMessagePartial[Message] {
        case Stop =>
          ctx.log.info("Stopping the server http://{}:{}",
            binding.localAddress.getHostString,
            binding.localAddress.getPort)

          Behaviors.stopped
      }.receiveSignal {
        case (_, PostStop) =>
          binding.unbind()
          Behaviors.same
      }
    }

    def starting(wasStopped: Boolean): Behaviors.Receive[Message] = {
      Behaviors.receiveMessage[Message] {
        case StartFailed(cause) => throw new RuntimeException("Server failed to start", cause)
        case Started(binding) =>
          ctx.log.info("Server online at http://{}:{}",
            binding.localAddress.getHostString,
            binding.localAddress.getPort)

          if (wasStopped) ctx.self ! Stop
          running(binding)
        case Stop => starting(wasStopped = true)
      }
    }

    starting(wasStopped = false)
  }


  def main(args: Array[String]): Unit = {

    val system: ActorSystem[SkyApp2.Message] = ActorSystem(SkyApp2("localhost", 8080), "SkyServer")
  }
}
