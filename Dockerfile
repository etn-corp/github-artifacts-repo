FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"
RUN mkdir -p /workspace
WORKDIR /workspace
USER root 

ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps/
ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps.dist/
ADD drivers/ojdbc6.jar $CATALINA_HOME/lib/
EXPOSE 8080
#CMD ["catalina.sh", "run"]

ADD create_tomcat_context.sh /create_tomcat_context.sh
ADD run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]