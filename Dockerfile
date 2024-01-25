FROM maven:3.9.6-eclipse-temurin-21-alpine as build
WORKDIR /exam
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21 as deploy
COPY --from=build /exam/target/exam-0.0.1-SNAPSHOT.jar /exam/exam.jar
COPY --from=build /exam/months.csv /exam/months.csv
COPY --from=build /exam/input_data.csv /exam/input_data.csv
RUN rm -rf src target/dependency
WORKDIR /exam
EXPOSE 8080
CMD ["java", "-jar", "exam.jar"]