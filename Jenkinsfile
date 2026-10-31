
pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    environment {
        DOCKERHUB_USERNAME = 'sksagarkr'
        IMAGE_NAME = 'springboot-test-project'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKERHUB_USERNAME/$IMAGE_NAME:$BUILD_NUMBER .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USER" --password-stdin
                        docker push "$DOCKERHUB_USERNAME/$IMAGE_NAME:$BUILD_NUMBER"
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy Application') {
            steps {
                sh '''
                    docker rm -f springboot-test-app || true

                    docker run -d \
                        --name springboot-test-app \
                        --restart unless-stopped \
                        -p 8085:8080 \
                        "$DOCKERHUB_USERNAME/$IMAGE_NAME:$BUILD_NUMBER"
                '''
            }
        }
    }

    post {
        success {
            echo 'CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
    }
}
