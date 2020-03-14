pipeline {
    agent {
        node {
            label 'Sky_Akka'
            checkout scm
        }
    }

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