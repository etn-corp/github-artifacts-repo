FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps/
ADD target/TargetAccountPlanner.war $CATALINA_HOME/webapps.dist/
ADD drivers/ojdbc6.jar $CATALINA_HOME/lib/
EXPOSE 8080
#CMD ["catalina.sh", "run"]

USER root
RUN chgrp -R 0 $CATALINA_HOME/conf/  && chmod -R g=u $CATALINA_HOME/conf/
USER 185

ADD create_tomcat_context.sh /create_tomcat_context.sh
ADD run.sh /run.sh
#RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]