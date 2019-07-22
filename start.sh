#!/bin/bash

KAFKA_HOST=localhost
KAFKA_PORT=9092
echo "Waiting for kafka to launch on ${KAFKA_HOST}:${KAFKA_PORT}..."

while ! nc -z ${KAFKA_HOST} ${KAFKA_PORT}; do
    echo "Waiting for kafka to launch on ${KAFKA_HOST}:${KAFKA_PORT}..."
  sleep 0.1
done

java -jar ./paymentservice/target/scala-2.12/producer.jar &
java -jar ./MyTaxiPaymentsConsumer/target/scala-2.12/consumer.jar