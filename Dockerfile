FROM openjdk:23
WORKDIR /app
COPY /build/libs/order_manager-0.0.1-SNAPSHOT.jar /app/order_manager.jar
ENTRYPOINT ["java", "-jar", "order_manager.jar"]
