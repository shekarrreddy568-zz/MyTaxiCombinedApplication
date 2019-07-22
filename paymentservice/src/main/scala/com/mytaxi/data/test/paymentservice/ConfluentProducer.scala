package com.mytaxi.data.test.paymentservice

import java.util.Properties

import com.mytaxi.data.test.paymentservice.avro.User
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.common.serialization.StringSerializer

object ConfluentProducer extends LazyLogging {

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "payment_producer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getCanonicalName)
  props.put("schema.registry.url", "http://localhost:8081")

  val producer = new KafkaProducer[String, User](props)

  val key: String = "payment"

//  def send(topic: String, payload: String): Unit = {
//    val record = new ProducerRecord[String, String](topic, key, payload)
//    producer.send(record)
//  }

    def send(topic: String, payload: User): Unit = {
      val record = new ProducerRecord[String, User](topic, key, payload)
      producer.send(record)
    }

}
