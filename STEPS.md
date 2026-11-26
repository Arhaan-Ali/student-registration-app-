# HOW TO DO IT - Question 1 Full Steps (repeat in lab / viva)

## 0. Prereqs check
java -version ; git --version ; docker --version ; mvn -version
# If Maven missing (Windows): download apache-maven-3.9.9-bin.zip from archive.apache.org,
# unzip to C:\Users\<you>\tools, add ...\apache-maven-3.9.9\bin to PATH, reopen terminal, mvn -version
# Start Docker Desktop and wait until: docker ps   works

## 1. Project (already in this folder)
- src/main/resources/index.html  -> form: Student Name, Roll Number, Email, Course, Submit
- src/main/java/com/student/StudentServer.java -> tiny JDK HttpServer, serves form, POST /register shows success
- pom.xml -> Maven jar build, mainClass set
mvn clean package
java -jar target/student-registration-app-1.0.0.jar   # http://localhost:8080
# SCREENSHOT 1: form in browser   SCREENSHOT 2: BUILD SUCCESS terminal + target/*.jar

## 2. GitHub
git init -b main ; git add -A ; git commit -m "Student Registration App"
# On github.com: New repository -> name student-registration-app -> Create (EMPTY, no README)
git remote add origin https://github.com/<YOUR-USERNAME>/student-registration-app.git
git push -u origin main
# SCREENSHOT 3: GitHub repo page showing all files

## 3. JENKINS - Option A: lab-provided Jenkins (exam default)
1. Open Jenkins URL given in lab (usually http://localhost:8080) and login.
2. Manage Jenkins -> Tools -> Maven installations -> Name EXACTLY: Maven
   -> tick Install automatically -> Version 3.9.9 -> Save.   (Jenkinsfile uses tools { maven 'Maven' })
3. Dashboard -> New Item -> name: student-registration -> Pipeline -> OK.
4. In job config -> Pipeline section -> Definition: "Pipeline script from SCM"
   -> SCM: Git -> Repository URL: your GitHub repo URL above -> Branch: */main
   -> Script Path: Jenkinsfile -> Save.  (Repo already contains Jenkinsfile: Checkout -> Build -> Test)
   NOTE: Linux Jenkins uses sh in Jenkinsfile. On a Windows Jenkins agent change sh to bat.
5. Click "Build Now" (left side).
6. Wait for blue/green tick. Open the build number -> Console Output -> confirm BUILD SUCCESS / Finished: SUCCESS.
# SCREENSHOT 4: build page with green tick + console tail showing Finished: SUCCESS
# If build fails "script not yet approved": you used inline Pipeline script without sandbox.
# Fix: use "Pipeline script from SCM" (step 4) OR tick "Use Groovy Sandbox" under inline script.

## 3b. JENKINS - Option B: no lab Jenkins? Run your own in Docker (what was done here)
docker build -t student-jenkins -f jenkins/Dockerfile jenkins
#   (jenkins/Dockerfile = FROM jenkins/jenkins:lts-jdk17 + workflow-aggregator + git plugins)
docker run -d --name jenkins -p 8082:8080 -e JAVA_OPTS="-Djenkins.install.runSetupWizard=false" -v student_jenkins_home:/var/jenkins_home student-jenkins
# Open http://localhost:8082  (here: admin / admin123 auto-created; on fresh Jenkins use
# initialAdminPassword: docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword)
# Then do Option A steps 2-6 inside it. Local demo here ran: mvn -B clean package on /project -> Build #2 SUCCESS.

## 4. GitHub Actions
# File .github/workflows/maven.yml already in repo (push event -> JDK 17 -> mvn -B clean package)
git add -A ; git commit -m "ci" ; git push
# GitHub repo -> Actions tab -> open latest run -> wait for green tick
# SCREENSHOT 5: Actions run page green

## 5. Docker (local, no cloud/K8s)
docker build -t student-registration-app .
docker run -d -p 8080:8080 --name student-app student-registration-app
# If 8080 busy (bind error): docker run -d -p 8081:8080 --name student-app student-registration-app
docker ps ; docker images ; docker logs student-app
# Open http://localhost:8080 (or 8081) -> form loads -> fill -> Submit -> Registration Successful
# SCREENSHOT 6: docker ps/images terminal + browser form

## 6. Submission PDF
# Page 1: Name, Roll Number, Question 1. Then screenshots 1-6 in order above, one PDF.
# This machine: evidence/Student-Registration-Evidence-Q1.pdf (+ individual PNGs in evidence/)
