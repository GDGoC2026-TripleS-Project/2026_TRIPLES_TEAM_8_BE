# 1) build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# ✅ Gradle Wrapper 포함 전체 복사
COPY . .

# ✅ wrapper에 정의된 Gradle 버전 사용
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 2) run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
