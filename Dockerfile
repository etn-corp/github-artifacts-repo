FROM registry.redhat.io/jboss-eap-7/eap72-openshift

COPY target/*.war $JBOSS_HOME/standalone/deployments/
#COPY drivers/*.jar $JBOSS_HOME/standalone/deployments/
COPY standalone-openshift.xml $JBOSS_HOME/standalone/configuration/standalone-openshift.xml

USER root
RUN chgrp -R 0 $JBOSS_HOME/standalone/deployments/  && chmod -R g=u $JBOSS_HOME/standalone/deployments/
USER 185

EXPOSE 8080
