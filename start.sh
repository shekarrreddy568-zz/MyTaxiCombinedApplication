#!/bin/bash

KAFKA_HOST=localhost
KAFKA_PORT=9092
SCHEMA_REGISTRY_HOST=localhost
SCHEMA_REGISTRY_PORT=8081

echo "Waiting for kafka to launch on ${KAFKA_HOST}:${KAFKA_PORT}..."

while ! nc -z ${KAFKA_HOST} ${KAFKA_PORT}; do
    echo "Waiting for kafka to launch on ${KAFKA_HOST}:${KAFKA_PORT}..."
  sleep 0.1
done

echo "Waiting for schema registry to launch on ${SCHEMA_REGISTRY_HOST}:${SCHEMA_REGISTRY_PORT}..."

while ! nc -z ${SCHEMA_REGISTRY_PORT} ${SCHEMA_REGISTRY_PORT}; do
    echo "Waiting for schema registry to launch on ${SCHEMA_REGISTRY_PORT}:${SCHEMA_REGISTRY_PORT}..."
  sleep 0.1
done

java -jar ./paymentservice/target/scala-2.12/producer.jar >> /var/log/producer.log 2>&1 &
java -jar ./MyTaxiPaymentsConsumer/target/scala-2.12/consumer.jar >> /var/log/consumer.log 2>&1