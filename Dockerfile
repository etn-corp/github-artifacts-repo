FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

ARG ENV_NAME
ENV env_name = $ENV_NAME

RUN chmod 777 $CATALINA_HOME/conf
RUN chmod 777 $CATALINA_HOME/webapps
RUN chmod 777 $CATALINA_HOME/webapps.dist

ENV TZ=EDT

ADD drivers/ojdbc6.jar $CATALINA_HOME/lib/
ADD target/TargetAccountPlanner.war  $CATALINA_HOME/webapps/
ADD target/TargetAccountPlanner/WEB-INF/classes/${env_name}  $CATALINA_HOME/webapps/TargetAccountPlanner/WEB-INF/classes/
#ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps.dist/


ADD create_tomcat_context.sh /create_tomcat_context.sh
ADD run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]