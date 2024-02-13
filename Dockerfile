FROM eclipse-temurin:17-jdk-focal

RUN mkdir /app

WORKDIR /app

COPY target/E-CommerceShop-0.0.1-SNAPSHOT.jar /app

EXPOSE 8090

ENTRYPOINT ["java","-jar","E-CommerceShop-0.0.1-SNAPSHOT.jar"]