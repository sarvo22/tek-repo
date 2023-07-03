FROM openjdk:8-jdk-alpine3.7
RUN mkdir /sar-app
COPY . /sar-app
RUN apk --no-cache add maven && mvn --version
RUN mvn clean && mvn compiler:compile && mvn package
EXPOSE 8080
CMD ["java", "-jar", "sar-app/target/inventory-serv.jar"]