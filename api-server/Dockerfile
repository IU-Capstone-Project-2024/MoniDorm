#
# BUILD STAGE
#
FROM maven AS build
COPY api-server/src /home/app/src
COPY .git /home/app/.git
COPY api-server/pom.xml /home/app
RUN mvn -f /home/app/pom.xml -Dskip.unit.tests=true clean package

#
# PACKAGE STAGE
#
FROM eclipse-temurin:21-alpine
COPY --from=build /home/app/target/monidorm-server.jar /home/app/app.jar
WORKDIR /home/app
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
