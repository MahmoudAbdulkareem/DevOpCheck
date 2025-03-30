pipeline {
    agent any

    environment {
        NEXUS_REPO = '192.168.33.10:5000'
        IMAGE_NAME = 'gestion-station-ski'
        IMAGE_TAG = 'latest'
        NEXUS_USER = 'admin'
        NEXUS_PASSWORD = '12345678'
        MAVEN_REPOSITORY_URL = 'https://maven.pkg.github.com/MahmoudAbdulkareem/DevOpCheck'  // GitHub Maven repository URL
        GITHUB_USERNAME = 'MahmoudAbdulkareem'  // Your GitHub username
    }

    stages {
        stage('GIT') {
            steps {
                sh 'rm -rf DevOpCheck'  // Remove the existing directory
                sh 'git clone --branch Mahmoud https://github.com/MahmoudAbdulkareem/DevOpCheck.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.token=squ_0c0407aeb3894cad6a0538dec2817899fce9af74'
            }
        }

       stage('Nexus Deploy') {
           steps {
               withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {

                   // Run the Maven deploy command to deploy to GitHub Packages
                   sh 'mvn deploy -DrepositoryId=github-repository -Durl=${MAVEN_REPOSITORY_URL} -s settings.xml -DskipTests'
               }
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
