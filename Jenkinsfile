pipeline {
    agent any
    tools { maven 'Maven' }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Build')    { steps { sh 'mvn -B clean package' } }
        stage('Test')     { steps { sh 'mvn -B test' } }
    }
    post { success { echo 'Jenkins Build SUCCESS' } }
}
