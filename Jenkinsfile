pipeline {
    agent any

    tools {
        maven 'Maven 3.3.9'
        jdk 'Java 8'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        VERSION = currentBuild.number
    }

    stages {
		stage('Checkout') {
			steps {
				git 'https://github.com/dschadow/ApplicationIntrusionDetection'
			}
		}

		stage('Version') {
			steps {
                sh "mvn -B versions:set -DnewVersion=$VERSION"
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