#!/bin/bash

docker-compose kill
docker-compose rm -f

rm -rf volumes

docker-compose up -d