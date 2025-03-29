pipeline {
    agent any

    environment {
        JAVA_HOME = "/C:/Program Files (x86)/Java/jdk-17.0.12/"
        M2_HOME = "/opt/apache-maven-3.6.3"
        PATH = "$M2_HOME/bin:$PATH"
        SONAR_HOST_URL = 'http://192.168.33.10:9000'
        SONAR_LOGIN = 'squ_4234086c09c0c3d568f52b3303480e43ed7d9426'
        NEXUS_REPO = '192.168.33.10:5000'
        IMAGE_NAME = 'gestion-station-ski'
        IMAGE_TAG = 'latest'
        NEXUS_USER = 'admin'
        NEXUS_PASSWORD = '12345678'
    }

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
