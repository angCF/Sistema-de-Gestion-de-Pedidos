#!/bin/sh

while ! nc -z gateway 8080; do
    echo "Esperando a Gateway Server..."
    sleep 3
done
echo "✅ Gateway Server disponible."

while ! nc -z mysql-db 3306; do
    echo "Esperando a MySQL de órdenes..."
    sleep 3
done
echo "✅ MySQL de órdenes disponible."

until curl -s -o /dev/null -w "%{http_code}" http://gateway:8080/api/producto | grep -q "200"; do
    echo "Esperando a Productos vía Gateway en la red de docker..."
    sleep 3
done
echo "✅ Productos disponible vía Gateway."

echo "🚀 Todos los servicios están disponibles. Iniciando la aplicación de órdenes..."
exec java -jar /app.jar

