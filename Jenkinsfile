pipeline {
    agent any

    environment {
        NEXUS_REPO = '192.168.33.10:8081'
        IMAGE_NAME = 'gestion-stationski'
        IMAGE_TAG = 'latest'
        NEXUS_USER = 'admin'
        NEXUS_PASSWORD = 'MahmoodAbdul12'
    }

    stages {
        stage('Clone Repository') {
            steps {
                sh 'rm -rf DevOpCheck'
                sh 'git clone --branch Mahmoud https://github.com/MahmoudAbdulkareem/DevOpCheck.git'
            }
        }

        stage('Update Version in POM') {
            steps {
                sh 'mvn versions:set -DnewVersion=1.3.6-SNAPSHOT'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.token=${SONAR_TOKEN}'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                 sh 'mvn deploy -DskipTests'
             }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build \
                    --build-arg NEXUS_USER=${NEXUS_USER} \
                    --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} \
                    -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'NEXUS_CREDENTIALS', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}
                        docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo "Deployment Successful!"
        }
        failure {
            echo "Deployment Failed! Check logs."
        }
    }
}
