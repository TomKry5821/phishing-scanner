FROM openjdk:25-jdk
WORKDIR /scanner
COPY build/libs/phishing-scanner-0.0.1-SNAPSHOT.jar scanner.jar
USER 1001
ENTRYPOINT ["java", "-jar", "scanner.jar"]
