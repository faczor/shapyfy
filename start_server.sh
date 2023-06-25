#!/bin/bash
mvn clean package -Dskiptest
docker build -t shapyfy .
docker run -dp 127.0.0.1:8080:8080 shapyfy:latest