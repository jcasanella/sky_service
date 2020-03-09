package org.sky.service.routes

object SkyResponses {

  sealed trait Status
  object Successful extends Status
  object Failed extends Status

  sealed trait Response
  case object OK2 extends Response
  final case class KO(message: String) extends Response

  val OK = "Ok"
  val ORDER_CREATED = "Order created"
}
