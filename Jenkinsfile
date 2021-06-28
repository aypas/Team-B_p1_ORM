node {

    stage("stage 1") {
        echo "hello world"
        sh "find"
        checkout scm
        withCredentials([string(credentialsId: 'sonar_auth_token', variable: 'sonar_auth_token')]) {
            println "${sonar_auth_token}"
            sh "mvn clean package"
            sh "mvn verify sonar:sonar -Dsonar.login=${sonar_auth_token} -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=2105-may24-devops -Dsonar.projectKey=2105-may24-devops_project1_team1B_ORM"
        }
    }
}