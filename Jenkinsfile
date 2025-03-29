pipeline {
    agent any



    stages {
        stage('GIT') {
            steps {
                git branch: 'mahmoud', url: 'https://github.com/LaameriSayf/DevopsSkiStation.git'
            }
        }

        stage('Compile Stage') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                bat 'mvn test'
            }
        }

      
        stage('Nexus Deploy') {
            steps {
                bat 'mvn deploy -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push to Nexus') {
            steps {
                script {
                    sh "docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}"
                    sh "docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo "Pipeline executed successfully."
        }
        failure {
            echo "Pipeline execution failed. Check logs for details."
        }
    }
}
