FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

ADD target/*.war ${catalina.home}/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]