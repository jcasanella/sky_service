FROM jenkins/jenkins:lts
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/
RUN /usr/local/bin/install-plugins.sh sbt:1.5 nexus-jenkins-plugin:3.8.20200310-130318.c482b58 docker-plugin:1.1.9