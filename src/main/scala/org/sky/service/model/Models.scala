package org.sky.service.model

import java.util.Date

sealed trait Entity
final case class Customer(personalId: String, name: String, surname: String, dob: Date, address: String,
                          zipcode: String, city: String) extends Entity