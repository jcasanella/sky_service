package org.sky.service.model

import java.util.Date

final case class Item(name: String, id: Long)
final case class Order(items: List[Item])
final case class Order2(id: Long, items: List[Item])
final case class Message(body: String)

sealed trait Entity
final case class Customer(personalId: String, name: String, surname: String, dob: Date, address: String,
                          zipcode: String, city: String) extends Entity