#A minimal container image with prebuilt artifact
FROM openjdk:17
EXPOSE 8080

COPY vertx-app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

HEALTHCHECK --start-period=30s \
    --interval=30s \
    --timeout=3s \
    --retries=3 \
    CMD ["bash", "-c", "curl -i -s http://127.0.0.1:9990/actuator/health | grep -E 'HTTP/1.1 (200|202)'"]