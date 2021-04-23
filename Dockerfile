FROM openjdk:8-jre
COPY target/xaplus-test-ledger-1.0.0.jar /opt/xaplus-test-ledger.jar
WORKDIR /opt
CMD ["java", "-jar", "xaplus-test-ledger.jar"]