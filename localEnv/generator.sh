#!/bin/bash

DELAY=10
PORTS=(10001 10002 10003)
SIZE=${#PORTS[@]}

RQ_UID=1

while :; do
    INDEX=$(($RANDOM%SIZE))
    PORT=${PORTS[INDEX]}

    FROM_USER=$(($RANDOM%3000))
    TO_USER=$(($RANDOM%3000))
    COUNT=$((1+$RANDOM%1000))

    if [[ ${FROM_USER} -ne ${TO_USER} ]]; then
        CMD="curl -s -X POST http://localhost:${PORT}/transfer?rqUid=${RQ_UID}&fromUser=${FROM_USER}&toUser=${TO_USER}&count=${COUNT}"
        RESULT=$(${CMD})
        echo ${CMD}, ${RESULT}
        sleep ${DELAY}

        RQ_UID=$((RQ_UID+1))
    fi
done