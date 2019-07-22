#!/usr/bin/env bash

cat >/import.cql <<EOF
DROP keyspace payments;
CREATE KEYSPACE payments WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;
CREATE TABLE payments.logs (
id int,
event_date bigint,
tour_value double,
id_driver int,
id_passenger int,
PRIMARY KEY (id)
);
EOF

# You may add some other conditionals that fits your stuation here
until cqlsh -f /import.cql; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"