package org.sky.service.service

import org.sky.service.model.Customer
import scala.concurrent.{ExecutionContext, Future}

object WrapperService {

  implicit val customerWrapped = new Operations[Customer] {

    var customers: Map[String, Customer] = Map.empty

    override def add(customer: Customer)(implicit ec: ExecutionContext): Future[Option[Customer]] = {
      Future {
        customer match {
          case Customer(id, _, _, _, _, _, _) =>
            customers += (id -> customer)
            Some(customer)

          case _ => None
        }
      }
    }

    override def get()(implicit ec: ExecutionContext): Future[Seq[Customer]] = Future {
      customers.values.toSeq
    }
  }
}