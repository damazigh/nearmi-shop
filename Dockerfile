FROM openjdk:14.0.2-jdk-oracle
ARG VERSION
ARG PROFILE
ARG JAR_FILE=target/shop-${VERSION}.jar
COPY ${JAR_FILE} shop.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${PROFILE}","/shop.jar"]