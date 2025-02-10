# Build
# This image has both Amazon Corretto 17 AND Maven 3.9.4 installed
FROM openjdk:17-jdk-slim as build

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests

#  Runtime 
FROM openjdk:17-jdk-slim

WORKDIR /app

# Set the timezone to IST (Asia/Kolkata) in runtime stage
RUN ln -sf /usr/share/zoneinfo/Asia/Kolkata /etc/localtime && echo "Asia/Kolkata" > /etc/timezone

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Add this line after the base image
RUN mkdir -p /var/log/dropbox/archived && \
    chmod -R 777 /var/log/dropbox

ENV JAVA_OPTS="-Dspring.devtools.restart.enabled=true -Dspring.devtools.add-properties=true"
ENV SPRING_DEVTOOLS_REMOTE_SECRET=mysecret

EXPOSE 8080
EXPOSE 35729

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
