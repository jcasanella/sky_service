package org.sky.service.service

import org.sky.service.model.{Customer, Entity}

import scala.concurrent.{ExecutionContext, Future}

trait Operations[A <: Entity] {

  def add(elem: A)(implicit ec: ExecutionContext): Future[Option[A]]

  def get()(implicit ec: ExecutionContext): Future[Seq[A]]
}

object PimpedOperations {

  def add[A <: Entity](value: A)(implicit ops: Operations[A], ec: ExecutionContext): Future[Option[A]] = {
    ops.add(value)
  }

  def get[A <: Entity](implicit ops: Operations[A], ec: ExecutionContext): Future[Seq[A]] = {
    ops.get()
  }
}


