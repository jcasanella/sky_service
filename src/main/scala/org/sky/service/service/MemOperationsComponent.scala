package org.sky.service.service

import org.sky.service.model.{Entity, Customer}
import scala.concurrent.{ExecutionContext, Future}

trait MemDbOperationsServiceComponent[B, A <: Entity] {

  val dBOperationsService: DbOperationsService[B, A]
  class MemDbOperationsService extends DbOperationsService[B, A] {
    var elems: Map[B, A] = Map.empty

    override def add(key: B, elem: A)(implicit ec: ExecutionContext): Future[A] = {
      Future {
        elems += (key -> elem)
        elem
      }
    }

    override def get()(implicit ec: ExecutionContext): Future[Seq[A]] = Future {
      elems.values.toSeq
    }

    override def get(key: B)(implicit ec: ExecutionContext): Future[A] = Future {
      elems(key)
    }
  }
}

trait CustomerServiceComponent {

  this: MemDbOperationsServiceComponent[String, Customer] =>

  val customerService: CustomerService
  class CustomerService {

    def add(customer: Customer)(implicit ec: ExecutionContext) : Future[Option[Customer]] = {
      customer match {
        case Customer(id, _, _, _, _, _, _) => dBOperationsService.add(id, customer).map(x => Some(x))

        case _ => Future { None }
      }
    }

    def get()(implicit ec: ExecutionContext): Future[Seq[Customer]] = dBOperationsService.get()

    def get(key: String)(implicit ec: ExecutionContext): Future[Customer] = dBOperationsService.get(key)
  }
}




