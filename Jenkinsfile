pipeline {
  agent any
  triggers {
    pollSCM('H/5 * * * *')
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
    git 'https://github.com/LinuxSuRen/phoenix.webui.framework.git'
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
