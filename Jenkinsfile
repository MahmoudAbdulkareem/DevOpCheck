pipeline {
    agent any

    environment {
        NEXUS_REPO = '192.168.33.10:5000'
        IMAGE_NAME = 'gestion-station-ski'
        IMAGE_TAG = 'latest'
        MAVEN_REPOSITORY_URL = 'https://maven.pkg.github.com/MahmoudAbdulkareem/DevOpCheck'
        GITHUB_USERNAME = 'MahmoudAbdulkareem'
    }

    stages {
        stage('GIT') {
            steps {
                sh 'rm -rf DevOpCheck'
                // 🔧 Fix 1: Use correct credential ID case (GithubToken)
                withCredentials([string(credentialsId: 'GithubToken', variable: 'GITHUB_TOKEN')]) {
                    sh """
                        git clone --branch Mahmoud \
                        https://${GITHUB_USERNAME}:${GITHUB_TOKEN}@github.com/${GITHUB_USERNAME}/DevOpCheck.git
                    """
                }
            }
        }

        stage('Maven Settings') {
            steps {
                // 🔧 Fix 2: Consistent credential ID & proper variable substitution
                withCredentials([string(credentialsId: 'GithubToken', variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        mkdir -p ~/.m2
                        echo "<settings>
                            <servers>
                                <server>
                                    <id>github</id>
                                    <username>$GITHUB_USERNAME</username>
                                    <password>$GITHUB_TOKEN</password>
                                </server>
                            </servers>
                            <repositories>
                                <repository>
                                    <id>github</id>
                                    <url>$MAVEN_REPOSITORY_URL</url>
                                </repository>
                            </repositories>
                        </settings>" > ~/.m2/settings.xml
                    '''
                }
            }
        }

        // [Rest of the stages remain unchanged...]
    }
}
