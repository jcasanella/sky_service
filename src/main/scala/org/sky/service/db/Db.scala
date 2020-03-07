package org.sky.service.db

import akka.Done
import org.sky.service.model.{Item, Order}

import scala.concurrent.{ExecutionContextExecutor, Future}

class Db {

  var orders = List[Item]()

  def fetchItem(itemId: Long)(implicit ec: ExecutionContextExecutor): Future[Option[Item]] = Future {
    orders.find(_.id == itemId)
  }

  def saveOrder(order: Order)(implicit ec: ExecutionContextExecutor): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }

    Future {
      Done
    }
  }
}
