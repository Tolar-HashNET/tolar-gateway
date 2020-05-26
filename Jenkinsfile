pipeline {
    agent any
    options { disableConcurrentBuilds() }

    environment {
        SLACK_URL   = credentials('slackUrl')
        SLACK_TOKEN = credentials('slackToken')
        SLACK_TEAM  = credentials('slackTeam')
    }

    stages {
        stage('Deploy Gateway MainNet') {
            when { branch 'master' }

            steps {
                sh 'mvn clean spring-boot:build-image'

                sh 'docker save dreamfactoryhr/tolar-gateway:latest ' +
                ' | ssh -C admin@172.31.7.104 sudo docker load'

                sh 'ssh -C admin@172.31.7.104 "sudo docker ps -f name=tolar-gateway-main -q ' +
                ' | xargs --no-run-if-empty sudo docker container stop ' +
                ' | xargs --no-run-if-empty sudo docker container rm"'

                sh 'ssh admin@172.31.7.104 sudo docker run -d ' +
                ' -e "SPRING_PROFILES_ACTIVE=prod"  --network=host ' +
                ' --name tolar-gateway-main --user 1001:1001 ' +
                ' dreamfactoryhr/tolar-gateway:latest '

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Gateway* connected to *MAIN NETWORK* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "JSON-RPC available at: " +
                            "<https://tolar.dream-factory.hr|tolar.dream-factory.hr>\n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: 'test-results', color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Deploy Gateway Staging') {
            when { branch 'develop' }

            steps {
                sh 'mvn clean spring-boot:build-image -Pstaging -DprofileIdEnabled=true'

                sh 'docker save dreamfactoryhr/tolar-gateway:staging ' +
                ' | ssh -C admin@172.31.7.104 sudo docker load'

                sh 'ssh -C admin@172.31.7.104 "sudo docker ps -f name=tolar-gateway-staging -q ' +
                ' | xargs --no-run-if-empty sudo docker container stop ' +
                ' | xargs --no-run-if-empty sudo docker container rm"'

                sh 'ssh admin@172.31.7.104 sudo docker run -d ' +
                ' -e "SPRING_PROFILES_ACTIVE=staging"  --network=host ' +
                ' --name tolar-gateway-staging --user 1001:1001 ' +
                ' dreamfactoryhr/tolar-gateway:staging '

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Gateway* connected to *STAGING NETWORK* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "JSON-RPC available at: " +
                            "<https://tolar-staging.dream-factory.hr|tolar-staging.dream-factory.hr>\n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: 'test-results', color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Deploy Node MainNet') {
            when { branch 'tolar-node' }

            steps {
                sh 'docker-compose build'
                sh 'docker save tolar-node:latest | ssh -C admin@172.31.7.104 sudo docker load'
                sh 'scp docker-compose.yaml admin@172.31.7.104:/home/admin/tolar-gateway/docker-compose.yaml'
                sh 'ssh -C admin@172.31.7.104 "cd tolar-gateway; sudo docker-compose down"'
                sh 'ssh -C admin@172.31.7.104 "cd tolar-gateway; sudo docker-compose up -d"'

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Node* connected to *MainNet* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: 'test-results', color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Docker Cleanup') {
            steps {
                sh 'docker system prune -f'

                script {
                    if (env.BRANCH_NAME == 'tolar-node') {
                        sh 'docker rmi tolar-node'
                    } else if (env.BRANCH_NAME == 'develop') {
                        sh 'docker rmi dreamfactoryhr/tolar-gateway:staging'
                    } else {
                        sh 'docker rmi dreamfactoryhr/tolar-gateway'
                    }
                }

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
