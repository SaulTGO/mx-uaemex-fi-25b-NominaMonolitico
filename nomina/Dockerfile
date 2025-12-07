FROM azul/zulu-openjdk:17
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
EXPOSE 1234
ENTRYPOINT ["java","-jar","target/nomina-0.0.1-SNAPSHOT.jar"]