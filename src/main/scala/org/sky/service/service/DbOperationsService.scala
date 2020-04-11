package org.sky.service.service

import org.sky.service.model.{Customer, Entity}

import scala.concurrent.{ExecutionContext, Future}

trait DbOperationsService[B, A <: Entity] {

  def add(id: B, elem: A)(implicit ec: ExecutionContext): Future[A]

  def get()(implicit ec: ExecutionContext): Future[Seq[A]]

  def get(key: B)(implicit ec: ExecutionContext): Future[A]
}

object DbRegistry extends CustomerServiceComponent with MemDbOperationsServiceComponent[String, Customer] {

  override val customerService = new CustomerService
  override val dBOperationsService: DbOperationsService[String, Customer] = new MemDbOperationsService
}
