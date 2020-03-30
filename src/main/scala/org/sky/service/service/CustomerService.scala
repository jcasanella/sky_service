package org.sky.service.service

import org.sky.service.model.Customer
import scala.concurrent.{ExecutionContext, Future}

object CustomerService {

  var customers: Map[String, Customer] = Map.empty

  def addCustomer(customer: Customer)(implicit ec: ExecutionContext): Future[Option[Customer]] = {
    Future {
      customer match {
        case Customer(id, _, _, _, _, _, _) =>
          customers += (id -> customer)
          Some(customer)

        case _ => None
      }
    }
  }

  def getCustomers()(implicit ec: ExecutionContext): Future[Seq[Customer]] = Future {
    customers.values.toSeq
  }
}

