# Student Registration App (Question 1)
Simple Student Registration form (Student Name, Roll Number, Email, Course) served by a minimal Java app (JDK built-in HttpServer, no database).

## Run locally with Maven
    mvn package
    java -jar target/student-registration-app-1.0.0.jar
Open http://localhost:8080

## Run with Docker
    docker build -t student-registration-app .
    docker run -d -p 8080:8080 --name student-app student-registration-app

## CI
- Jenkins: see Jenkinsfile (Checkout -> Build -> Test)
- GitHub Actions: see .github/workflows/maven.yml (runs mvn package on push)
