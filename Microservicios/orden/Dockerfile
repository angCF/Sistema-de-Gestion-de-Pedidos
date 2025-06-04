FROM maven AS build

WORKDIR /app-src

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests
RUN ls -la /app-src/target

FROM azul/zulu-openjdk-alpine:21 AS server
RUN apk add --no-cache netcat-openbsd curl

WORKDIR /

VOLUME /tmp
COPY --from=build /app-src/target/app.jar .

COPY start.sh /start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]