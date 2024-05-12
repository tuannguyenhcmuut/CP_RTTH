pipeline {
    agent any
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose-server.yml'
    }
    stages {
        
        stage('Build') {
            steps {
                // Thêm các lệnh build ứng dụng của bạn ở đây
                sh 'echo Building...'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dir('oms-server') {
                        sh 'docker compose -f ${DOCKER_COMPOSE_FILE} up -d --no-deps --build'
                    }
                }
            }
        }
    }
    post {
        // test
    }
}