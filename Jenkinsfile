pipeline {
    agent any

   environment {
       IMAGE_NAME = 'mahmoudabdulkareem1/gestion-stationski'
       IMAGE_TAG = 'latest'

      NEXUS_PROTOCOL = 'http'
      NEXUS_HOST = '192.168.33.10'
      NEXUS_PORT = '8081'
      NEXUS_REPO_URL = "${NEXUS_PROTOCOL}://${NEXUS_HOST}:${NEXUS_PORT}/repository/${NEXUS_REPO}/"
       NEXUS_REPO = 'gestionski'  // Make sure this matches the repository name in Nexus


       NEXUS_CREDENTIAL_ID = 'NEXUS_CREDENTIAL'
       DOCKERHUB_CREDENTIALS = credentials('Docker_ID')
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
                        sh "mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_REPO_URL} -s .jenkins/settings.xml -e"
                    }
                }
            }
        }

        stage('DOCKER IMAGE') {
            steps {
                script {
                    def startTime = System.currentTimeMillis()
                    try {
                        sh '''
                        echo "Building Docker image..."
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                        '''
                    } finally {
                        def endTime = System.currentTimeMillis()
                        def duration = (endTime - startTime) / 1000
                        echo "Docker image build duration: ${duration}s"
                    }
                }
            }
        }

        stage('DOCKER HUB') {
            steps {
                script {
                    def startTime = System.currentTimeMillis()
                    try {
                        sh '''
                        echo "Logging into Docker Hub..."
                        echo "${DOCKERHUB_CREDENTIALS_PSW}" | docker login -u "${DOCKERHUB_CREDENTIALS_USR}" --password-stdin
                        echo "Pushing image to Docker Hub..."
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        '''
                    } finally {
                        def endTime = System.currentTimeMillis()
                        def duration = (endTime - startTime) / 1000
                        echo "Docker Hub push duration: ${duration}s"
                    }
                }
            }
        }

        stage('DOCKER-COMPOSE') {
            steps {
                script {
                    def startTime = System.currentTimeMillis()
                    try {
                        sh '''
                        set -e
                        echo "Checking for docker-compose.yml..."
                        ls -la
                        if [ ! -f docker-compose.yml ]; then
                            echo "Error: docker-compose.yml not found!"
                            exit 1
                        fi
                        echo "Checking Docker image..."
                        docker images | grep ${IMAGE_NAME}
                        echo "Stopping and removing mysql-test container if it exists..."
                        if docker ps -a --format '{{.Names}}' | grep -q "^mysql-test$"; then
                            echo "Stopping and removing mysql-test container..."
                            docker stop mysql-test || true
                            docker rm mysql-test || true
                        fi
                        echo "Checking ports in use..."
                        docker ps -a --format '{{.Names}} {{.Ports}}'
                        echo "Exporting IMAGE_TAG for Docker Compose..."
                        export IMAGE_TAG=${IMAGE_TAG}
                        echo "Starting Docker Compose with IMAGE_TAG=${IMAGE_TAG}..."
                        docker compose up -d --build
                        echo "Checking running containers..."
                        docker compose ps
                        '''
                    } finally {
                        def endTime = System.currentTimeMillis()
                        def duration = (endTime - startTime) / 1000
                        echo "Docker Compose duration: ${duration}s"
                    }
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
