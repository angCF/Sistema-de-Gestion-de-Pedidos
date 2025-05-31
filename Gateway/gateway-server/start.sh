#!/bin/sh

while ! nc -z eureka 8761 ; do
    echo "Waiting for the Eureka Server"
    sleep 3
done

exec java -jar /app.jar
