pipeline {
    agent any
    options { disableConcurrentBuilds() }

    environment {
        SLACK_URL   = credentials('slackUrl')
        SLACK_TOKEN = credentials('slackToken')
        SLACK_TEAM  = credentials('slackTeam')
    }

    stages {
        stage('Build Gateway Image') {
            when { not { branch 'tolar-node' } }

            steps {
                sh 'mvn clean install dockerfile:build'
            }
        }

        stage('Deploy Gateway to Staging') {
            when { branch 'master' }

            steps {
                sh 'ssh -i id_rsa ~/.ssh/ -C admin@172.31.7.104 "echo kita"'

                sh 'docker save dreamfactoryhr/tolar-gateway:latest ' +
                ' | ssh -C admin@172.31.7.104 sudo docker load'

                sh 'ssh -C admin@172.31.7.104 "sudo docker ps -f name=tolar-gateway -q ' +
                ' | xargs --no-run-if-empty sudo docker container stop ' +
                ' | xargs --no-run-if-empty sudo docker container rm"'

                sh 'ssh admin@172.31.7.104 sudo docker run -d ' +
                ' -e "SPRING_PROFILES_ACTIVE=dev"  --network=host ' +
                ' --name tolar-gateway --user 1001:1001 ' +
                ' dreamfactoryhr/tolar-gateway:latest '

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Gateway* to *STAGING* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: 'test-results', color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Deploy Node to Staging') {
            when { branch 'tolar-node' }

            steps {
                sh 'docker-compose build'
                sh 'docker save tolar-node:latest | ssh -C admin@172.31.7.104 sudo docker load'
                sh 'scp docker-compose.yaml admin@172.31.7.104:/home/admin/tolar-gateway/docker-compose.yaml'
                sh 'ssh -C admin@172.31.7.104 "cd tolar-gateway; sudo docker-compose down"'
                sh 'ssh -C admin@172.31.7.104 "cd tolar-gateway; sudo docker-compose up -d"'

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Node* to *STAGING* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: 'test-results', color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Docker cleanup Jenkins') {
            steps {
                sh 'docker system prune -f'
            }
        }

        stage('Docker cleanup Jenkins Gateway') {
            when { not { branch 'tolar-node' } }

            steps {
                sh 'docker rmi dreamfactoryhr/tolar-gateway'
            }
        }

        stage('Docker cleanup Jenkins Node') {
            when { branch 'tolar-node' }

            steps {
                sh 'docker rmi tolar-node'
            }
        }

        stage('Docker cleanup Staging') {
            when { anyOf { branch 'master'; branch 'tolar-node'} }

            steps {
                sh 'ssh -C admin@172.31.7.104 "sudo docker system prune -f"'
            }
        }
    }


    post {
        success {
            script {
                if (env.BRANCH_NAME == 'noMessageHack') {
                    slackSend(channel: 'test-results', color: 'good',
                            message: "Success \'${env.JOB_NAME} [${env.BUILD_NUMBER}]\'" +
                                    " (<${env.RUN_DISPLAY_URL}|Pipeline>)",
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        failure {
            script {
                slackSend(channel: 'test-results', color: 'danger',
                        message: "FAILED \'${env.JOB_NAME} [${env.BUILD_NUMBER}]\'" +
                                " (<${env.RUN_DISPLAY_URL}|Pipeline>)",
                        teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
            }
        }
    }

}
