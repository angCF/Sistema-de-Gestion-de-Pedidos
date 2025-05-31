#!/bin/sh

# Esperar a que el Gateway esté disponible
while ! nc -z gateway 8080; do
    echo "Esperando a Gateway Server..."
    sleep 3
done
echo "✅ Gateway Server disponible."

# Esperar a que la base de datos de productos esté disponible
while ! nc -z mysql-db-products 3306; do
    echo "Esperando a MySQL de productos..."
    sleep 3
done
echo "✅ MySQL de productos disponible."

# Iniciar la app
echo "🚀 Todos los servicios están disponibles. Iniciando la aplicación de productos..."
exec java -jar /app.jar
