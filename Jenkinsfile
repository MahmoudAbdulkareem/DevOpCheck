pipeline {
    agent any

    environment {
     
      
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

       stage('SonarQube Analysis') {
    steps {
        bat 'mvn sonar:sonar -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.login=squ_4234086c09c0c3d568f52b3303480e43ed7d9426'
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
                    bat "docker build -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push to Nexus') {
            steps {
                script {
                    bat "docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}"
                    bat "docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}"
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
