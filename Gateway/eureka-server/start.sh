#!/bin/sh

while ! nc -z config 8888 ; do
    echo "Esperando al Config Server..."
    sleep 3
done

exec java -jar /app.jar
