pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Pulling the Code...'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                sh "${tool name: 'sbt-1.3.8', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean compile"
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
}