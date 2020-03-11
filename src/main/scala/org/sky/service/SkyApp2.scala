package org.sky.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import org.sky.service.db.ActionRepository
import org.sky.service.routes.SkyRoutes2

import scala.util.{Failure, Success}

object SkyApp2 {

  private def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {

    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    val futureBinding = Http().bindAndHandle(routes, "localhost", 8080)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {

    // server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val actionRegistryActor = context.spawn(ActionRepository(), "ActionRegistryActor")
      context.watch(actionRegistryActor)

      val routes = new SkyRoutes2(actionRegistryActor)(context.system)
      startHttpServer(routes.routes2, context.system)

      Behaviors.empty
    }

    ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }
}


