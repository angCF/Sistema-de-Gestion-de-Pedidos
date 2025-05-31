#Descarga dependencias y compila cod
FROM maven AS build

WORKDIR /app-src

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests
#Verifica la existencia del .jar
RUN ls -la /app-src/target

#Construye imagen final
FROM azul/zulu-openjdk-alpine:21 AS server
ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}
WORKDIR /

VOLUME /tmp
COPY --from=build /app-src/target/app.jar .

#Ejecuta la app
ENTRYPOINT [ "java", "-jar","app.jar" ]
#COPY start.sh .
#RUN chmod +x start.sh

#ENTRYPOINT ["/start.sh"]