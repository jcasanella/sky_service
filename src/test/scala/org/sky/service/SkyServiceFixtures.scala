package org.sky.service

trait SkyServiceFixtures {

  val custId1 = "1"
  val custId2 = "2"
  val customer1 = s"""{"address":"124 London Road","city":"London","dob":"1979-12-29","name":"George","personalId":"${custId1}","surname":"Smith","zipcode":"MK12"}"""
  val customer2 = s"""{"address":"421 Manchester Road","city":"Wolverton","dob":"1979-11-29","name":"Sophie","personalId":"${custId2}","surname":"Larrou","zipcode":"WO12"}"""
  val customers = Seq(customer1, customer2)
}
