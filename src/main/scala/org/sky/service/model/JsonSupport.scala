package org.sky.service.model

import java.text.SimpleDateFormat
import java.util.Date
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, _}
import scala.util.Try


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object DateFormat extends JsonFormat[Date] {
    def write(date: Date) = JsString(dateToIsoString(date))

    def read(json: JsValue) = json match {
      case JsString(rawDate) =>
        parseIsoDateString(rawDate)
          .fold(deserializationError(s"Expected ISO Date format, got $rawDate"))(identity)
      case error => deserializationError(s"Expected JsString, got $error")
    }

    private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
      override def initialValue() = new SimpleDateFormat("yyyy-MM-dd")
    }

    private def dateToIsoString(date: Date) =
      localIsoDateFormatter.get().format(date)

    private def parseIsoDateString(date: String): Option[Date] =
      Try{ localIsoDateFormatter.get().parse(date) }.toOption
  }

  implicit val customerFormat = jsonFormat7(Customer)
}
