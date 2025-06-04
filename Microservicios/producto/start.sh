#!/bin/sh

while ! nc -z gateway 8080; do
    echo "Esperando a Gateway Server..."
    sleep 3
done
echo "✅ Gateway Server disponible."

while ! nc -z mysql-db-products 3306; do
    echo "Esperando a MySQL de productos..."
    sleep 3
done
echo "✅ MySQL de productos disponible."

echo "🚀 Todos los servicios están disponibles. Iniciando la aplicación de productos..."
exec java -jar /app.jar
