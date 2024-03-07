FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

USER root 

RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src

RUN mvn -B package --file pom.xml -DskipTests -Dskip

RUN chmod 777 $CATALINA_HOME/conf
RUN chmod 777 $CATALINA_HOME/webapps
RUN chmod 777 $CATALINA_HOME/webapps.dist

ENV TZ=EDT
FROM openjdk:14-slim
COPY --from=build /workspace/target/*.war $CATALINA_HOME/webapps/
COPY --from=build /workspace/target/*.war $CATALINA_HOME/webapps.dist/
ADD drivers/ojdbc6.jar $CATALINA_HOME/lib/
EXPOSE 8080

ADD create_tomcat_context.sh /create_tomcat_context.sh
ADD run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]