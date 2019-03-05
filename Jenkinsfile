node {
        stage("Checkout") {
            checkout scm
        }

        stage('Maven Build') {
            sh "mvn -DskipTests=true package"
        }

        stage('Maven Tests') {
            sh "mvn test"
            sh "echo Testing"
        }

        stage('Docker image') {
             docker.build("catalogue")
        }

        stage("Remove old Container") {
            sh "docker rm -f catalogue || echo 'ok'"
        }

        stage("Deploy") {
            sh "docker run -d --name catalogue --net shop -p 2020:8080 catalogue"
        }
}
