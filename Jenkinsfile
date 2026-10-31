pipeline {
    agent any

    parameters {
    string(
        name: 'DEPLOY_PORT',
        defaultValue: '8085',
        description: 'Enter the port for Spring Boot deployment'
    )

    choice(
        name: 'DEPLOY_ENV',
        choices: ['DEV', 'TEST', 'PROD'],
        description: 'Select deployment environment'
    )
}

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
                echo "Selected environment: ${DEPLOY_ENV}"
                echo "Selected deployment port: ${DEPLOY_PORT}"
    
                docker rm -f springboot-test-app || true
    
                docker run -d \
                    --name springboot-test-app \
                    --restart unless-stopped \
                    -p ${DEPLOY_PORT}:8080 \
                    -e APP_ENV=${DEPLOY_ENV} \
                    "$DOCKERHUB_USERNAME/$IMAGE_NAME:$BUILD_NUMBER"
            '''
        }
    }
        
        stage('Verify Deployment') {
            steps {
                sh '''
                    echo "Waiting for Spring Boot application..."

                    for i in $(seq 1 30); do
                        if curl -fsS http://host.docker.internal:${DEPLOY_PORT}/actuator/health; then
                            echo ""
                            echo "Application is healthy!"
                            exit 0
                        fi

                        echo "Waiting... attempt $i"
                        sleep 2
                    done

                    echo "Application health check failed!"
                    docker logs springboot-test-app
                    exit 1
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
