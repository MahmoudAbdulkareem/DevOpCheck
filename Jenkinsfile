pipeline {
    agent any

    environment {
        NEXUS_REPO = '192.168.33.10:8081'
        IMAGE_NAME = 'gestion-stationski'
        IMAGE_TAG = 'latest'
        NEXUS_USER = 'admin'
        NEXUS_PASSWORD = 'MahmoodAbdul12'
    }

    pipeline {
        agent any

        tools {
            maven 'MAVEN'
        }

        environment {
            // Nexus and SonarQube configuration
            NEXUS_PROTOCOL = 'http'
            NEXUS_HOST = '192.168.33.10'
            NEXUS_PORT = '8081'
            NEXUS_REPO = "${NEXUS_HOST}:${NEXUS_PORT}"
            NEXUS_VERSION = 'nexus3'
            NEXUS_REPOSITORY = 'gestionski'
            NEXUS_CREDENTIAL_ID = 'NEXUS_CREDENTIALS'

            SONAR_URL = 'http://192.168.33.10:9000'
            IMAGE_NAME = 'devopcheck'
            IMAGE_TAG = 'latest'
        }

        stages {
            stage('Clone Repository') {
                steps {
                    sh 'rm -rf DevOpCheck'
                    sh 'git clone --branch Mahmoud https://github.com/MahmoudAbdulkareem/DevOpCheck.git'
                    dir('DevOpCheck') {
                        script {
                            // Set working directory for all following steps
                            env.WORK_DIR = pwd()
                        }
                    }
                }
            }

            stage('Update Version in POM') {
                steps {
                    dir("${env.WORK_DIR}") {
                        sh 'mvn versions:set -DnewVersion=1.3.6-SNAPSHOT'
                    }
                }
            }

            stage('Compile') {
                steps {
                    dir("${env.WORK_DIR}") {
                        sh 'mvn clean compile'
                    }
                }
            }

            stage('Run Tests') {
                steps {
                    dir("${env.WORK_DIR}") {
                        sh 'mvn test'
                    }
                }
            }

            

            stage('Deploy to Nexus') {
                steps {
                    dir("${env.WORK_DIR}") {
                        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh 'mvn deploy -DskipTests'
                        }
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    dir("${env.WORK_DIR}") {
                        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh """
                                docker build \
                                --build-arg NEXUS_USER=${NEXUS_USER} \
                                --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} \
                                -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} .
                            """
                        }
                    }
                }
            }

            stage('Push Docker Image to Nexus') {
                steps {
                    dir("${env.WORK_DIR}") {
                        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh """
                                docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}
                                docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}
                            """
                        }
                    }
                }
            }

            stage('Deploy with Docker Compose') {
                steps {
                    dir("${env.WORK_DIR}") {
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

