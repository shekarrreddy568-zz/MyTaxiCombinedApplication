package com.mytaxi.data.test.payments

import com.datastax.driver.core.Cluster
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.{LazyLogging, Logger}


/** This calss aggregates the events produced by the payments service
  *
  *  @constructor create a new even with id, event_date, tour_value, id_driver, id_passenger
  *  @param id unique event ID
  *  @param event_date date in milli sec when the even is occured
  *  @param tour_value amount of the transction
  *  @param id_driver unique driver ID
  *  @param id_passenger Passenged ID
  */
case class Event(id: Int, event_date: Long, tour_value: Double, id_driver: Int, id_passenger: Int)

object EventStorageToCassandra extends LazyLogging {
  /** Creates a session with Cassandra database and
    * defines a method to insert the events into table
    */
  val cluster = Cluster.builder()
    .addContactPoint(ConfigFactory.load().getString("application.cassandra.host"))
    .withPort(ConfigFactory.load().getInt("application.cassandra.port"))
    .build()

  val session = cluster.connect()
  val keyspace = ConfigFactory.load().getString("application.cassandra.keyspace")
  val table_name = ConfigFactory.load().getString("application.cassandra.table")

  def insertIntoCassandraTable(event: Event) = {
    /** Insert the events into cassandra table
      *  @param event event object defines as the case class
      */
    try {
      session.execute(
        s"INSERT INTO $keyspace.$table_name (id, event_date, tour_value, id_driver, id_passenger) " +
          s"VALUES (${event.id}, ${event.event_date}, ${event.tour_value}, ${event.id_driver}, ${event.id_passenger})"
      )
      logger.info(s"event has been inserted into Cassandra table $keyspace.$table_name")
    }
    catch {
      case e: Exception => logger.info(e.getMessage)
    }

  }
}
