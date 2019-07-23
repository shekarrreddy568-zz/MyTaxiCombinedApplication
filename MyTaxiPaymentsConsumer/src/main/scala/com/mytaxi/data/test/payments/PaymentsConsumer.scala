package com.mytaxi.data.test.payments

import java.util
import java.util.Properties

import com.mytaxi.data.test.paymentservice.avro.User

import scala.util.parsing.json._
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import com.typesafe.scalalogging.LazyLogging

import org.apache.kafka.common.serialization.StringDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializer

object PaymentsConsumer extends LazyLogging {
  /** defines a method to create connection with Kafka Cluster and
    * defines a Consumer application , print the events to the log and
    * also insert the events into table
    */

  def getConsumerProperties: Properties = {
    /** Creates connection to the kafka cluster with
      * the supplied connection properties
      */

    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigFactory.load().getString("application.kafka.brokers"))
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, ConfigFactory.load().getString("application.client.id"))
    //  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ConfigFactory.load().getString("application.key.deserializer"))
    //  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ConfigFactory.load().getString("application.value.deserializer"))
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getCanonicalName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, ConfigFactory.load().getString("application.group.id"))
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ConfigFactory.load().getString("application.enable.auto.commit"))
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, ConfigFactory.load().getString("application.auto.commit.interval.ms"))
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ConfigFactory.load().getString("application.auto.offset.reset"))
    props.put("schema.registry.url", ConfigFactory.load().getString("application.schema.registry.url"))
    props.put("specific.avro.reader", ConfigFactory.load().getString("application.specific.avro.reader"))
    return props
  }

  def main(args: Array[String]): Unit = {

    logger.info("starting payments consumer application.......")
    val topics = ConfigFactory.load().getString("application.topic.name")

    try {

      val consumer = new KafkaConsumer[String, User](getConsumerProperties) // creating Consumer instance
      consumer.subscribe(util.Collections.singletonList(topics)) // subscribing to the topics

      while (true) {
        val records = consumer.poll(100) // polling for records
        val it = records.iterator()

        while (it.hasNext()) {

          val record = it.next()
          logger.info("key: " + record.key() + " , " + "value: " + record.value())

          val parsed = JSON.parseFull(record.value().toString) // parsing the json string to Json value

          parsed.foreach {
            case json: Map[String, Any] =>

              val id = json("id").asInstanceOf[Double].toInt
              val event_date = json("event_date").asInstanceOf[Double].toLong
              val tour_value = json("tour_value").asInstanceOf[Double]
              val id_driver = json("id_driver").asInstanceOf[Double].toInt
              val id_passenger = json("id_passenger").asInstanceOf[Double].toInt

              val event = Event(id, event_date, tour_value, id_driver, id_passenger) //Preparing the Event object with the extacted values
              logger.info(s"event: $event")

              EventStorageToCassandra.insertIntoCassandraTable(event) // calling the insertIntoCassandraTable fucntion
              consumer.commitSync() // comminting the offsets
          }
        }

      }
    }
    catch {
      case e: Exception => logger.info(e.getMessage)
    }
  }
}
