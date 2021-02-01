def buildCommand = 'mvn clean spring-boot:build-image'
def imageName = 'dreamfactoryhr/tolar-gateway:latest'
def remoteAddress = 'admin@172.31.7.104'
def containerName = 'tolar-gateway-main'
def springProfile = 'prod'

def targetNetwork = 'MAIN'
def gatewayLink = 'tolar.dream-factory.hr'

def slackChannel = 'deployments'
def defaultBuildMessage = " \'${env.JOB_NAME}\' [${env.BUILD_NUMBER}]" +
                         " (<${env.RUN_DISPLAY_URL}|Pipeline>)"

pipeline {
    agent any
    options { disableConcurrentBuilds() }

    environment {
        SLACK_URL   = credentials('slackUrl')
        SLACK_TOKEN = credentials('slackToken')
        SLACK_TEAM  = credentials('slackTeam')
    }

    stages {
        stage('Setup branch variable') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        buildCommand = buildCommand + ' -P test'

                        imageName = imageName.replace('latest', 'test')

                        containerName = containerName.replace('main', 'test')

                        springProfile = 'test'

                        targetNetwork = 'TEST'

                        gatewayLink = gatewayLink.replace('tolar', 'tolar-test')
                    } else if (env.BRANCH_NAME == 'staging') {
                        error('staging env is disabled')

                        buildCommand = buildCommand + ' -P staging'

                        imageName = imageName.replace('latest', 'staging')

                        containerName = containerName.replace('main', 'staging')

                        springProfile = 'staging'

                        targetNetwork = 'STAGING'

                        gatewayLink = gatewayLink.replace('tolar', 'tolar-staging')
                    } else if (env.BRANCH_NAME != 'master' && env.BRANCH_NAME != 'tolar-node') {
                        error('Invalid branch! ' + env.BRANCH_NAME)
                    }
                }
            }
        }

        stage('Deploy Gateway') {
            when {
                anyOf {
                    branch 'master';
                    branch 'develop';
                    branch 'staging';
                }
            }

            steps {
                sh buildCommand

                sh 'docker save ' + imageName +
                ' | ssh -C ' + remoteAddress + ' sudo docker load'

                sh 'ssh -C ' + remoteAddress + ' "sudo docker ps -f name=' + containerName + ' -q ' +
                ' | xargs --no-run-if-empty sudo docker container stop ' +
                ' | xargs --no-run-if-empty sudo docker container rm"'

                sh 'ssh -C ' + remoteAddress + ' sudo docker run -m 2g -d ' +
                ' -e "SPRING_PROFILES_ACTIVE=' + springProfile + '" ' +
                ' -e JAVA_OPTS="-Xmx1400m" --network=host --name ' + containerName +
                ' --user 1001:1001 ' + imageName

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Gateway* connected to *" + targetNetwork + " NETWORK* \n" +
                            "JSON-RPC available at: <https://" + gatewayLink + "|" + gatewayLink + ">\n" +
                            "Links: <${env.RUN_DISPLAY_URL}|Pipeline>, " +
                            "<https://tolar-clients.kwiki.io/docs/tolar-hashnet|Docs>, " +
                            "<https://hub.docker.com/r/dreamfactoryhr/tolar-gateway|DockerHub> \n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: slackChannel, color: 'good', message: slackMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        stage('Deploy Nodes') {
            when { branch 'tolar-node' }

            steps {
                unzip zipFile: 'thin_node_bin_1.0.02.zip', dir: 'thin_node'
                //unzip zipFile: 'thin_node_bin_1.0.02.zip', dir: 'staging_node'
                unzip zipFile: 'thin_node_bin_1.0.02.zip', dir: 'main_node'

                sh 'docker-compose build'
                sh 'docker save tolar-node:latest | ssh -C ' + remoteAddress + ' sudo docker load'
                //sh 'docker save staging-node:latest | ssh -C ' + remoteAddress + ' sudo docker load'
                sh 'docker save main-node:latest | ssh -C ' + remoteAddress + ' sudo docker load'
                sh 'scp docker-compose.yaml ' + remoteAddress + ':/home/admin/tolar-gateway/docker-compose.yaml'
                sh 'ssh -C ' + remoteAddress + ' "cd tolar-gateway; sudo docker-compose down"'
                sh 'ssh -C ' + remoteAddress + ' "cd tolar-gateway; sudo docker-compose up -d"'

                script {
                    def buildTime = currentBuild.durationString.replace(' and counting', '')

                    slackMessage = "Deployed *Tolar Nodes* connected to *MainNet*, *StagingNet* and *TestNet* (" +
                            "<${env.RUN_DISPLAY_URL}|Pipeline>" +
                            ") \n" +
                            "Pipeline time: ${buildTime}"

                    slackSend(channel: slackChannel, color: 'good', message: slackMessage,
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
                        //sh 'docker rmi staging-node'
                        sh 'docker rmi main-node'
                    } else {
                        sh 'docker rmi ' + imageName
                    }
                }

                sh 'ssh -C ' + remoteAddress + ' "sudo docker image prune -f"'
            }
        }
    }

    post {
        success {
            script {
                if (env.BRANCH_NAME == 'noMessageHack') {
                    slackSend(channel: slackChannel, color: 'good',
                            message: "Success" + defaultBuildMessage,
                            teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
                }
            }
        }

        failure {
            script {
                slackSend(channel: slackChannel, color: 'danger',
                        message: "FAILED" + defaultBuildMessage,
                        teamDomain: SLACK_TEAM, baseUrl: SLACK_URL, token: SLACK_TOKEN)
            }
        }
    }

}
