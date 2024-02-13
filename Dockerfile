FROM eclipse-temurin:17-jdk-focal
RUN mkdir /ecommerce-app
WORKDIR /app

COPY target/E-CommerceShop-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java","-jar","E-CommerceShop-0.0.1-SNAPSHOT.jar"]