package org.sky.service.db

import org.sky.service.model.Order2
import org.sky.service.routes.SkyResponses
import org.sky.service.routes.SkyResponses.Response

import akka.actor.typed.{ ActorRef, Behavior }
import akka.actor.typed.scaladsl.Behaviors

object ActionRepository {

  sealed trait Command
  final case class addOrder(order: Order2, replyTo: ActorRef[Response]) extends Command
  final case class getItem(id: Long, replyTo: ActorRef[Option[Order2]]) extends Command

  def apply(orders: Map[Long, Order2] = Map.empty): Behavior[Command] = {

    Behaviors.receiveMessage {
      case addOrder(order, replyTo) =>
        replyTo ! SkyResponses.OK2
        ActionRepository(orders + (order.id -> order))
      case getItem(id, replyTo) =>
        replyTo ! orders.get(id)
        Behaviors.same
    }
  }
}


