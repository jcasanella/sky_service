package org.sky.service

trait SkyServiceFixtures {

  val customer1 = """{"address":"124 London Road","city":"London","dob":"1979-12-29","name":"George","personalId":"1","surname":"Smith","zipcode":"MK12"}"""
  val customers = Seq(customer1)
}
