# App-Gesti-n-de-Inventario
App con arquitectura de microservicios diseñada para brindar funcionalidades básicas de un sistema gestor de inventario.

# Configuraicón base de datos MySQL

## Crear usuario con el comando:

CREATE USER 'movienow'@'localhost' IDENTIFIED BY 'Movie-now-123*';
GRANT ALL PRIVILEGES ON *.* TO 'movienow'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

## Crear nueva conexión 

Username: movienow
Password: Movie-now-123*

## Crear esquema

Nombre: movie_now