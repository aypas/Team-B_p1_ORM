node {

    stage("stage 1") {
        checkout scm
        echo "hello world"
        sh "find"
        withCredentials([string(credentialsId: 'sonar_auth_token', variable: 'sonar_auth_token')]) {
            println "${sonar_auth_token}"
            sh "mvn clean package"
            sh "mvn sonar:sonar -Dsonar.login=${sonar_auth_token}"
        }
    }
}