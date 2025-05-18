# App-Gesti-n-de-Inventario
App con arquitectura de microservicios diseñada para brindar funcionalidades básicas de un sistema gestor de inventario.

# Configuraicón base de datos MySQL

### Crear usuario con el comando:

-- Crear un nuevo usuario en MySQL
CREATE USER 'movienow'@'localhost' IDENTIFIED BY 'Movie-now-123*';

-- Otorgar todos los privilegios al usuario sobre todas las bases de datos y tablas
GRANT ALL PRIVILEGES ON *.* TO 'movienow'@'localhost' WITH GRANT OPTION;

-- Aplicar los cambios de privilegios
FLUSH PRIVILEGES;

### Crear nueva conexión 

Username: movienow
Password: Movie-now-123*

### Crear esquema

Nombre: movie_now
