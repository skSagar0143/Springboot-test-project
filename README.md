# Spring Boot Test Project

A small Java 21 + Spring Boot REST API used to practise GitHub, Jenkins CI/CD, Docker, Docker Hub and Kubernetes.

## Endpoints

- `GET /api/hello` — returns a welcome message and application name.
- `GET /actuator/health` — application health check.

## Technology

- Java 21
- Spring Boot 3.5.7
- Maven
- Docker
- Jenkins Pipeline
- Kubernetes

## Run locally

```bash
mvn spring-boot:run
```

Open `http://localhost:8080/api/hello` and `http://localhost:8080/actuator/health`.

## Build and test

```bash
mvn clean verify
```

## Docker

Build the JAR first, then build and run the image:

```bash
mvn clean package
# Replace YOUR_DOCKERHUB_USERNAME with your Docker Hub username
docker build -t YOUR_DOCKERHUB_USERNAME/springboot-test-project:1.0 .
docker run --rm -p 8080:8080 YOUR_DOCKERHUB_USERNAME/springboot-test-project:1.0
```

## Jenkins

The `Jenkinsfile` contains a declarative pipeline for checkout, test, package and Docker image build/push. Configure a Jenkins username/password credential with ID `dockerhub-creds` and update `DOCKERHUB_USERNAME` in the pipeline before running the push stage.

## Kubernetes

Update the image in `k8s/deployment.yaml` to your published Docker Hub image, then apply:

```bash
kubectl apply -f k8s/
kubectl get pods
kubectl get services
```

The Kubernetes Service is NodePort for local Docker Desktop Kubernetes practice.