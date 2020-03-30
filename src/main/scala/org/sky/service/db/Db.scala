package org.sky.service.db

import akka.Done
import org.sky.service.model.{Customer, Item, Order}

import scala.concurrent.{ExecutionContextExecutor, Future}

class Db(implicit ec: ExecutionContextExecutor) {

  var orders = List[Item]()
  var customers: Map[String, Customer] = Map.empty

  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(_.id == itemId)
  }

  def saveOrder(order: Order): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }

    Future {
      Done
    }
  }

//  def addCustomer(Customer: Customer): Future[Customer] = {}
}
