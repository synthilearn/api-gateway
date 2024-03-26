FROM curlimages/curl:8.2.1 AS download
ARG OTEL_AGENT_VERSION="1.32.0"
RUN curl --silent --fail -L "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$HOME/opentelemetry-javaagent.jar"

FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
ADD . /build
RUN cd /build && mvn package --quiet -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /build/target/*.jar /app.jar
COPY --from=download /home/curl_user/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ARG GIT_USERNAME
ARG GIT_PASSWORD

ENV GIT_USERNAME=${GIT_USERNAME}
ENV GIT_PASSWORD=${GIT_PASSWORD}
ENV JAVA_TOOL_OPTIONS="-javaagent:/opentelemetry-javaagent.jar"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://62.76.228.98:4327"
ENV OTEL_EXPORTER_OTLP_TRACES_ENDPOINT="http://62.76.228.98:4327"
ENV OTEL_EXPORTER_OTLP_PROTOCOL="grpc"
ENV OTEL_SERVICE_NAME="gateway-server"
ENV OTEL_TRACES_EXPORTER="otlp"
ENV OTEL_METRICS_EXPORTER="otlp"
ENV OTEL_LOGS_EXPORTER="otlp"
ENV SPRING_PROFILES_ACTIVE="dev"

ENTRYPOINT ["java", \
  "-javaagent:/opentelemetry-javaagent.jar", \
  "-jar", "/app.jar" \
  ]