# Sistema de Gestión de Pedidos Simplificado
Aplicación con arquitectura de microservicios diseñada para brindar funcionalidades básicas de un sistema gestor de pedidos simplificado

### 📦 Requisitos Previos
Docker + Docker Compose

(Opcional) Java 21+ y Maven para ejecución local

### 🔐 Configuración del archivo .env
Antes de levantar los servicios, copia el archivo de ejemplo **.env.example** a un nuevo archivo llamado **.env** en la raíz del proyecto.

### 🔧 Ajusta los valores según tus necesidades.
Este archivo contiene las credenciales y configuraciones necesarias para levantar las bases de datos MySQL de los microservicios.

### 🚀 Ejecución con Docker Compose
```console
docker-compose --env-file .env up --build
```
Esto construirá y levantará todos los microservicios junto con el servidor Eureka.

✅ Importante: En este modo, los microservicios se comunican entre sí usando los nombres de contenedor como hostnames. Por ejemplo, la URL de Eureka configurada en cada microservicio será:

application.properties
```console
eureka.client.service-url.defaultZone=http://eureka:8761/eureka/
```
Aquí, eureka es el nombre del servicio en docker-compose.yml, y Docker lo resuelve como hostname dentro de su red interna.

### ⚠️ Ejecución local (sin Docker)
Si decides ejecutar los microservicios manualmente con Maven:

bash
```console
./mvnw spring-boot:run
```
💥 Debes cambiar manualmente la URL de Eureka en application.properties de cada microservicio, estos archivos los encontrarás en el config-server/src/resources/configurations, por ejemplo:

ordenes.properties
```console
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```
De lo contrario, el microservicio intentará registrarse en http://eureka:8761, lo que fallará localmente porque eureka no es resolvible fuera de Docker.
