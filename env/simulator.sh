#!/bin/bash

DELAY=5
SERVICES=($(docker-compose ps --services))
SIZE=${#SERVICES[@]}

while :; do
    INDEX=$(($RANDOM%SIZE))
    SERVICE=${SERVICES[INDEX]}

    docker-compose kill ${SERVICE}
    sleep ${DELAY}
    docker-compose up -d ${SERVICE}
    sleep ${DELAY}

    STEP=$((STEP+1))
done