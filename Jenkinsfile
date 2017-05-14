pipeline {
    agent any

    tools {
        maven 'Maven 3.3.9'
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
				sh 'mvn -B clean package'
			}
		}

		stage('Archive') {
			steps {
				archiveArtifacts(artifacts: '**/target/*.jar', fingerprint: true)
				junit(testResults: '**/target/**TEST*.xml', allowEmptyResults: true)
			}
		}
	}
}