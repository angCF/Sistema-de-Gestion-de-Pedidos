#!/bin/sh

# Esperar a que el Gateway esté disponible
while ! nc -z gateway 8080; do
    echo "Esperando a Gateway Server..."
    sleep 3
done
echo "✅ Gateway Server disponible."

# Esperar a que la base de datos de órdenes esté disponible
while ! nc -z mysql-db 3306; do
    echo "Esperando a MySQL de órdenes..."
    sleep 3
done
echo "✅ MySQL de órdenes disponible."

# Esperar a que el endpoint de productos esté disponible a través del Gateway
until curl -s -o /dev/null -w "%{http_code}" http://gateway:8080/api/producto | grep -q "200"; do
    echo "Esperando a Productos vía Gateway (http://gateway:8080/api/producto)..."
    sleep 3
done
echo "✅ Productos disponible vía Gateway."


# Iniciar la app
echo "🚀 Todos los servicios están disponibles. Iniciando la aplicación de órdenes..."
exec java -jar /app.jar

