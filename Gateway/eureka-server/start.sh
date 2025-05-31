#!/bin/sh

# Espera a que el Config Server esté disponible
while ! nc -z config 8888 ; do
    echo "Esperando al Config Server..."
    sleep 3
done

# Ejecuta la app
exec java -jar /app.jar
