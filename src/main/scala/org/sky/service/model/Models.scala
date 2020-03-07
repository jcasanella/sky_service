package org.sky.service.model

final case class Item(name: String, id: Long)
final case class Order(items: List[Item])
final case class Message(body: String)