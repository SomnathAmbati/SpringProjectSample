# Use lightweight Java image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only required files first (better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give permission
RUN chmod +x mvnw

# Download dependencies first (faster rebuilds)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

# Expose port (Render uses dynamic PORT)
EXPOSE 8080


# Run app
CMD ["sh", "-c", "java -jar target/*.jar"] 