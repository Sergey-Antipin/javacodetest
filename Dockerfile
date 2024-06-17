FROM maven:3.9.6-eclipse-temurin-8 as buildwar
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM tomcat:8.5.100-jdk8
WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*
COPY --from=buildwar /app/target/javacodetest webapps/javacodetest
EXPOSE 8080
CMD ["catalina.sh", "run"]