package org.sky.service.model

final case class Item(name: String, id: Long)
final case class Order(items: List[Item])
final case class Order2(id: Long, items: List[Item])
final case class Message(body: String)