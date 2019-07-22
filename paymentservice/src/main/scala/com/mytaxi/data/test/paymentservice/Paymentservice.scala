package com.mytaxi.data.test.paymentservice

import com.mytaxi.data.test.paymentservice.avro.User
import com.typesafe.scalalogging.LazyLogging

object Paymentservice extends App with LazyLogging {
  logger.info("starting paymentservice...")
  while (true) {
    val topic = "payments"
    val r = scala.util.Random
    val id = r.nextInt(10000000)
    val tour_value = r.nextDouble() * 100
    val id_driver = r.nextInt(10)
    val id_passenger = r.nextInt(100)
    val event_date = System.currentTimeMillis
//    val payload =
//      s"""
//         |{ "id": $id,
//         |   "event_date": $event_date,
//         |   "tour_value": $tour_value,
//         |   "id_driver": $id_driver,
//         |   "id_passenger": $id_passenger
//         |}
//      """.stripMargin

    val payload = User(id, event_date, tour_value, id_driver, id_passenger)
    logger.info(s"payload: ${payload.toString}")

    ConfluentProducer.send(topic, payload)
    logger.info(s"record has been sent to kafka topic")
    Thread.sleep(1000)
  }
}
