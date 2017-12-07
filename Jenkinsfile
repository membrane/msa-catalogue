node {
        stage("Checkout") {
            checkout scm
        }

        stage('Maven Build') {
            sh "echo $SHELL"
            sh "mvn package"
        }

        stage('Docker image') {
             docker.build("catalogue")
        }

        stage("Deploy") {
            sh "docker rm -f catalogue || echo 'ok'"
            sh "docker run -d --name catalogue --net shop -p 8080:8080 catalogue"
        }
}
