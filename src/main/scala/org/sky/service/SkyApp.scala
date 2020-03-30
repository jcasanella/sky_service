package org.sky.service

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http

import scala.io.StdIn
import org.sky.service.routes.SkyRoutes2

object SkyApp extends App {

  implicit val system = ActorSystem("SkyServiceApp")
  implicit val materializer = ActorMaterializer
  implicit val executionContext = system.dispatcher

  val log = Logging(system, "SpecialRoutes")
  val skyRoutes = new SkyRoutes2(log)
  val bindingFuture = Http().bindAndHandle(skyRoutes.routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080\nPress return to stop")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}


