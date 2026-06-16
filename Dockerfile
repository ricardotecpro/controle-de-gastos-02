# Estágio 1: Build da Aplicação com OpenJDK
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Imagem Final de Execução (mais leve)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]