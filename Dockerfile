FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

RUN chmod 777 $CATALINA_HOME/conf
RUN chmod 777 $CATALINA_HOME/webapps
RUN chmod 777 $CATALINA_HOME/webapps.dist

ENV TZ=EDT

COPY drivers/ojdbc6.jar $CATALINA_HOME/lib/
COPY target/TargetAccountPlanner  $CATALINA_HOME/webapps/TargetAccountPlanner/
COPY target/TargetAccountPlanner/WEB-INF/classes/${env_name}/com  $CATALINA_HOME/webapps/TargetAccountPlanner/WEB-INF/classes/com
COPY target/TargetAccountPlanner/WEB-INF/classes/${env_name}/conf  $CATALINA_HOME/webapps/TargetAccountPlanner/WEB-INF/classes/conf

#ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps.dist/


COPY create_tomcat_context.sh /create_tomcat_context.sh
COPY run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]