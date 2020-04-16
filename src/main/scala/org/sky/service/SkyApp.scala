package org.sky.service

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory

import scala.io.StdIn
import org.sky.service.routes.SkyRoutes

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SkyApp extends App {

  implicit val system = ActorSystem("SkyServiceApp")
  implicit val materializer = ActorMaterializer
  implicit val executionContext = system.dispatcher

  val ip_port = {
    val config = ConfigFactory.load()
    (config.getString("sky-app.interface"), config.getInt("sky-app.port"))
  }

  val log = Logging(system, "SpecialRoutes")
  val skyRoutes = new SkyRoutes(log)
  val bindingFuture = Http().bindAndHandle(skyRoutes.routes, ip_port._1, ip_port._2)

  println(s"Server online at http://${ip_port._1}:${ip_port._2}\nPress return to stop")
  StdIn.readLine()

  val onceAllConnectionsTerminated: Future[Http.HttpTerminated] =
    Await.result(bindingFuture, 10.seconds)
      .terminate(hardDeadline = 3.seconds)

  onceAllConnectionsTerminated.flatMap( _ => system.terminate())
}


