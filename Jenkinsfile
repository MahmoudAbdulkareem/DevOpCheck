pipeline {
    agent any

    environment {
        NEXUS_REPO = '192.168.33.10:5000'
        IMAGE_NAME = 'gestion-station-ski'
        IMAGE_TAG = 'latest'
        NEXUS_USER = 'admin'

        // Securely retrieve credentials from Jenkins
        NEXUS_PASSWORD = credentials('NEXUS_PASSWORD')  // Jenkins Secret for Nexus
        MAVEN_REPOSITORY_URL = 'https://maven.pkg.github.com/MahmoudAbdulkareem/DevOpCheck'
        GITHUB_USERNAME = 'MahmoudAbdulkareem'
        GITHUB_TOKEN = credentials('GITHUB_PAT')  // Jenkins Secret for GitHub Token
        SONAR_TOKEN = credentials('SONARQUBE_TOKEN')  // Jenkins Secret for SonarQube
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo "🛠️ Cloning repository..."
                sh 'rm -rf DevOpCheck || true'  // Ensure no leftover directory
                sh 'git clone --branch Mahmoud https://github.com/MahmoudAbdulkareem/DevOpCheck.git'
            }
        }

        stage('Compile Code') {
            steps {
                echo "🛠️ Compiling code..."
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo "🧪 Running tests..."
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "🔍 Running SonarQube analysis..."
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.token=${SONAR_TOKEN}'
            }
        }

        stage('Deploy to GitHub Packages') {
            steps {
                echo "📦 Deploying artifacts to GitHub Packages..."
                script {
                    writeFile file: 'settings.xml', text: """<?xml version="1.0" encoding="UTF-8"?>
                    <settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
                              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
                        <servers>
                            <server>
                                <id>github-repository</id>
                                <username>${env.GITHUB_USERNAME}</username>
                                <password>${env.GITHUB_TOKEN}</password>
                            </server>
                        </servers>
                    </settings>"""

                    sh 'mvn deploy -DrepositoryId=github-repository -Durl=${MAVEN_REPOSITORY_URL} -s settings.xml -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Building Docker image..."
                sh "docker build -t ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                script {
                    echo "📤 Pushing Docker image to Nexus..."
                    sh "docker login -u ${NEXUS_USER} -p ${NEXUS_PASSWORD} ${NEXUS_REPO}"
                    sh "docker push ${NEXUS_REPO}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "🚀 Deploying using Docker Compose..."
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline executed successfully!"
        }
        failure {
            echo "❌ Pipeline failed! Check logs for errors."
        }
    }
}
