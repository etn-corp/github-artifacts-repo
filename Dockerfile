FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps/
ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps.dist/

EXPOSE 8080
CMD ["catalina.sh", "run"]