pipeline {
    agent any

    tools {
        maven 'Maven 3.5.0'
        jdk 'Java 8'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    triggers {
        pollSCM '@daily'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/dschadow/ApplicationIntrusionDetection'
            }
        }

        stage('Version') {
            steps {
                sh "mvn versions:set -DnewVersion=${env.BUILD_NUMBER}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B package sonar:sonar'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts(artifacts: '**/target/*.jar', fingerprint: true)
                junit(testResults: '**/target/**TEST*.xml', allowEmptyResults: true)
            }
        }
    }

    post {
        failure {
            step {
                def to = emailextrecipients([
                        [$class: 'DevelopersRecipientProvider']
                ])
                def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} Failure"
                def content = '${JELLY_SCRIPT,template="html"}'

                if (to != null && !to.isEmpty()) {
                    emailext(body: content, mimeType: 'text/html',
                            replyTo: '$DEFAULT_REPLYTO', subject: subject,
                            to: to, attachLog: true)
                }
            }
        }
    }
}