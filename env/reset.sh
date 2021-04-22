#!/bin/bash

docker-compose kill

rm -rf ledger-1
rm -rf ledger-2
rm -rf ledger-3

docker-compose up -d