FROM eclipse-temurin:17.0.10_7-jre-jammy
WORKDIR /app
COPY ./target/ElGrande*.jar ./elgrande.jar
COPY ./docker/application.yaml ./
CMD ["java", "-jar", "./elgrande.jar"]

