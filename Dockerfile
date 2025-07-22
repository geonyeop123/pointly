# Build stage
FROM eclipse-temurin:21 AS build

WORKDIR /app

# 프로젝트 전체 복사
COPY . .

# Gradle 빌드
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# 빌드된 jar 복사
COPY --from=build /app/build/libs/pointly.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]