#!/bin/bash

# Espera hasta que ambos servicios (Eureka y MySQL) estén disponibles
while true; do
    # Comprobar si Eureka está disponible
    if nc -z eureka 8761; then
        echo "Eureka Server está disponible"
    else
        echo "Esperando a Eureka Server..."
    fi

    # Comprobar si MySQL está disponible
    if nc -z mysql-db 3306; then
        echo "MySQL está disponible"
    else
        echo "Esperando a MySQL..."
    fi

    # Si ambos servicios están disponibles, salimos del ciclo
    if nc -z eureka 8761 && nc -z mysql-db 3306; then
        echo "Eureka y MySQL están disponibles. Iniciando la aplicación..."
        break
    fi

    # Espera antes de volver a comprobar
    sleep 3
done

# Inicia la aplicación Spring Boot
exec java -jar /app.jar
