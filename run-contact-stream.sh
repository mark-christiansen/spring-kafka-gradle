#!/bin/bash

VERSION="1.0"
APP="contact-stream"

echo ""
echo "setup environment variables for the ${APP} to be run"
export APP_ID=${APP}
export BOOTSTRAP_URL=localhost:9092
export CONTACT_ADDRESS_TOPIC=canonical.contactaddress
export CONTACT_TOPIC=source.contact
export ERROR_TOPIC=error.contact
export GROUP_ID=${APP}
export HTTP_PORT=8802
export IN_MEMORY_STATE=false
export METRICS_RECORDING_LEVEL=TRACE
export OUTPUT_TOPIC=canonical.contact
export ROCKSDB_BLOCK_SIZE_KB=4
export ROCKSDB_INDEX_FILTER_BLOCK_RATIO=0.2
export ROCKSDB_MEMTABLE_INSTANCES=3
export ROCKSDB_MEMTABLE_SIZE_MB=16
export ROCKSDB_BLOCKCACHE_MEMORY_MB=50
export SCHEMA_URL=http://localhost:8081
export STATE_DIR=platform/volumes/${APP}-1/data
export STATE_STORE_CLEANUP=false

echo ""
echo "startup ${APP}"
java -jar ${APP}/target/${APP}-${VERSION}.jar