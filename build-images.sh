#!/bin/bash

# For deployment
# docker build -t account-service ./account-service/
# docker build -t course-service ./course-service/

# docker compose up -d


# For development
cd account-service
./start.sh
cd ..
cd course-service
./start.sh
cd ..

docker-compose up
