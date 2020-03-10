package org.sky.service.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.sky.service.model.{Item, Order2}
import org.sky.service.routes.SkyResponses.{Failed, Status, Successful}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

trait SkyResponsesJson extends SprayJsonSupport {

  import DefaultJsonProtocol._
  import org.sky.service.db.ActionRepository._

  implicit object StatusFormat extends RootJsonFormat[Status] {

    def write(status: Status): JsValue = {
      status match {
        case Failed => JsString("Failed")
        case Successful => JsString("Successful")
      }
    }

    def read(json: JsValue): Status = {
      json match {
        case JsString("Failed") => Failed
        case JsString("Successful") => Successful
        case _ => throw new DeserializationException("Status unexpected")
      }
    }
  }

  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat2(Order2)
}
