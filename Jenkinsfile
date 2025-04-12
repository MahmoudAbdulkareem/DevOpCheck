pipeline {
    agent any

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
                withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    dir('DevOpCheck') {
                        sh "mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_REPO_URL} -s .jenkins/settings.xml"
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('DevOpCheck') {
                    withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh '''
                            echo "Blackcat12" | docker login -u "mahmoudabdulkareem" --password-stdin ${NEXUS_HOST}:${NEXUS_PORT}
                            docker build \
                                --build-arg NEXUS_USER=$NEXUS_USER \
                                --build-arg NEXUS_PASSWORD=$NEXUS_PASSWORD \
                                -t ${NEXUS_HOST}:${NEXUS_PORT}/${IMAGE_NAME}:${IMAGE_TAG} .
                        '''
                    }
                }
            }
        }




        stage('Push Docker Image to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh '''
                        echo "$NEXUS_PASSWORD" | docker login -u "$NEXUS_USER" --password-stdin ${NEXUS_HOST}:${NEXUS_PORT}
                        docker push ${NEXUS_HOST}:${NEXUS_PORT}/${IMAGE_NAME}:${IMAGE_TAG}
                    '''
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
