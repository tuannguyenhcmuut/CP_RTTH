pipeline {
    agent any
    environment {
        DOCKER_COMPOSE_FILE = './oms-server/docker-compose-server.yml'
    }
    stages {
        stage('Check Docker') {
            steps {
                script {
                    // Check if Docker is available on Windows
                    def dockerExists = bat(script: "where docker", returnStatus: true) == 0
                    if (!dockerExists) {
                        error("Docker is not installed or not in the PATH")
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                // Thêm các lệnh build ứng dụng của bạn ở đây
                bat 'echo Building...'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                        bat 'docker compose -f ${DOCKER_COMPOSE_FILE} up -d --no-deps --build'
                }
            }
        }
    }
}