node {
    stage("stage 1") {
        echo "hello world"
        withCredentials([string(credentialsId: 'sonar_auth_token', variable: 'sonar_auth_token')]) {
            println "${sonar_auth_token}"
        }
    }
}