# build an image a spring boot jar
FROM openjdk:14.0.2-jdk-oracle
ARG VERSION
ARG JAR_FILE=target/shop-${VERSION}.jar
COPY ${JAR_FILE} shop.jar
USER root
COPY rootCA.cer $JAVA_HOME/lib/security
RUN \
    cd $JAVA_HOME/lib/security \
    && keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias nearmiRootCA -file rootCA.cer
EXPOSE 8001
ENTRYPOINT ["java","-jar","/shop.jar"]