pipeline {
    agent any
  environment {
        MAVEN_HOME = tool name: 'Maven 3.9', type: 'maven'
    }
    environment {
        IMAGE_NAME = 'gestion-stationski'
        IMAGE_TAG = 'latest'

        NEXUS_PROTOCOL = 'http'
        NEXUS_HOST = '192.168.33.10'
        NEXUS_PORT = '8081'
        NEXUS_REPO = 'gestionski'
        NEXUS_REPO_URL = "${NEXUS_PROTOCOL}://${NEXUS_HOST}:${NEXUS_PORT}/repository/${NEXUS_REPO}/"
        NEXUS_CREDENTIAL_ID = 'NEXUS_CREDENTIAL'
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
                dir('DevOpCheck') {
                    sh 'mvn versions:set -DnewVersion=1.3.6-SNAPSHOT'
                }
            }
        }

        stage('Compile') {
            steps {
                dir('DevOpCheck') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('DevOpCheck') {
                    sh 'mvn test'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                 withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                                    sh """
                                        ${MAVEN_HOME}/bin/mvn clean deploy \
                                        -DaltDeploymentRepository=nexus::default::http://$NEXUS_USER:$NEXUS_PASS@192.168.33.10:8081/repository/gestionski/
                                    """
                                }

            }
        }

        stage('Build Docker Image') {
            steps {
                dir('DevOpCheck') {
                    withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh """
                            docker build \\
                            --build-arg NEXUS_USER=${NEXUS_USER} \\
                            --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} \\
                            -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} .
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}
                        docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                dir('DevOpCheck') {
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful!"
        }
        failure {
            echo "❌ Deployment Failed! Check logs."
        }
    }
}
