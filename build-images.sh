#!/bin/bash

docker build -t account-service ./account-service/
docker build -t course-service ./course-service/

# docker compose up -d
