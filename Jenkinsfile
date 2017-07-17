pipeline {
  agent any
  triggers {
    cron('* 1 * * *')
  }
  
  stages {
    stage('Example') {
      steps {
        echo 'hello jenkins'
      }
    }
  }
}

node {
  def mvnHome
  
  stage('Preparation') {
    git 'https://github.com/LinuxSuRen/autotest.report.jira.git'
    mvnHome = tool 'M3'
  }
  
  stage('Build') {
    if(isUnix()){
      sh "'${mvnHome}/bin/mvn' clean package"
    }else{
      bat(/"${mvnHome}\bin\mvn" clean package/)
    }
  }
}
