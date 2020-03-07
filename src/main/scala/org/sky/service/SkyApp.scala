package org.sky.service

import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http

import scala.io.StdIn
import org.sky.service.db.Db
import org.sky.service.routes.SkyRoutes

object SkyApp extends App {
  println("Begin App")

  implicit val system = ActorSystem("SkyServiceApp")
  implicit val materializer = ActorMaterializer
  implicit val executionContext = system.dispatcher

  val log = Logging(system, "SpecialRoutes")
  val skyRoutes = new SkyRoutes(log)
  val bindingFuture = Http().bindAndHandle(skyRoutes.routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080\nPress return to stop")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
