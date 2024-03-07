FROM maven:3.6.3-openjdk-14-slim AS build
ARG ENV_NAME
ENV env_name = $ENV_NAME
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src

RUN mvn -B package --file pom.xml -DskipTests -Dskip
FROM openjdk:14-slim
LABEL org.opencontainers.image.source="https://github.com/etn-corp/executive-vehicle-program"
COPY --from=build /workspace/target/*.jar evp.jar

FROM tomcat:9-jdk11-openjdk
LABEL maintainer="urvashisharma@eaton.com"

USER root 

RUN chmod 777 $CATALINA_HOME/conf
RUN chmod 777 $CATALINA_HOME/webapps
RUN chmod 777 $CATALINA_HOME/webapps.dist
ENV TZ=EDT

COPY --from=build /workspace/target/*.war $CATALINA_HOME/webapps/
COPY --from=build /workspace/target/*.war $CATALINA_HOME/webapps.dist/

ADD drivers/ojdbc6.jar $CATALINA_HOME/lib/
EXPOSE 8080

ADD create_tomcat_context.sh /create_tomcat_context.sh
ADD run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]