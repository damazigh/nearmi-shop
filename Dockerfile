FROM openjdk:14.0.2-jdk-oracle
ARG VERSION
ARG JAR_FILE=target/shop-${VERSION}.jar
COPY ${JAR_FILE} shop.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/shop.jar"]