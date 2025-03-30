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
         NEXUS_PASSWORD = credentials('NEXUS_PASSWORD')  // Nexus Password from Jenkins Credentials
         SONAR_TOKEN = credentials('SONARQUBE_TOKEN')  // SonarQube Token from Jenkins Credentials
     }

     stages {
         stage('Clone Repository') {
             steps {
                 echo "🛠️ Cloning repository..."
                 sh 'rm -rf DevOpCheck || true'  // Ensure no leftover directory
                 sh 'rm -rf DevOpCheck || true'
                 sh 'git clone --branch Mahmoud https://github.com/MahmoudAbdulkareem/DevOpCheck.git'
             }
         }
 @@ -48,21 +45,23 @@
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
                 withCredentials([string(credentialsId: 'GITHUB_PAT', variable: 'GITHUB_TOKEN')]) {
                     script {
                         writeFile file: 'settings.xml', text: """<?xml version="1.0" encoding="UTF-8"?>
                         <settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
                                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                   xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
                             <servers>
                                 <server>
                                     <id>github-repository</id>
                                     <username>MahmoudAbdulkareem</username>
                                     <password>\${env.GITHUB_TOKEN}</password>
                                 </server>
                             </servers>
                         </settings>"""

                     sh 'mvn deploy -DrepositoryId=github-repository -Durl=${MAVEN_REPOSITORY_URL} -s settings.xml -DskipTests'
                         sh 'mvn deploy -DrepositoryId=github-repository -Durl=https://maven.pkg.github.com/MahmoudAbdulkareem/DevOpCheck -s settings.xml -DskipTests'
                     }
                 }
             }
         }