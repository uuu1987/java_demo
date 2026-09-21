# ── 1단계: 빌드 전용 컨테이너 ──
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# 의존성 파일만 먼저 복사 (build.gradle이 안 바뀌면 이 레이어를 재사용해 빌드 속도 향상)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# 소스 코드는 그 다음에 복사 (코드만 바뀌었을 때 위 레이어는 캐시 재사용)
COPY src src

# 테스트는 이미 로컬에서 검증했으니 빌드 시간 단축을 위해 스킵
RUN ./gradlew build -x test --no-daemon

# ── 2단계: 실행 전용 컨테이너 ──
FROM eclipse-temurin:25-jre
WORKDIR /app

# 1단계에서 만들어진 jar만 가져옴 (Gradle/소스코드는 여기 안 남음)
COPY --from=build /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]