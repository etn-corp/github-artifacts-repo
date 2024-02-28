FROM jboss/base-jdk:7

# Set the JBOSSAS_VERSION env variable
ENV JBOSSAS_MAIN_VERSION 6.1
ENV JBOSSAS_VERSION 6.1.0.Final
ENV JBOSSAS_SHA1 d6cf1c809fd24d084939a1e18df4f8cbb04e10fb
ENV JBOSS_HOME /opt/jboss/as

USER root

# Add the JBoss AS distribution to /opt, and make jboss the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -O http://download.jboss.org/jbossas/$JBOSSAS_MAIN_VERSION/jboss-as-distribution-$JBOSSAS_VERSION.zip \
    && sha1sum jboss-as-distribution-$JBOSSAS_VERSION.zip | grep $JBOSSAS_SHA1 \
    && unzip jboss-as-distribution-$JBOSSAS_VERSION.zip \
    && mv $HOME/jboss-$JBOSSAS_VERSION $JBOSS_HOME \
    && rm jboss-as-distribution-$JBOSSAS_VERSION.zip \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

USER jboss

# Expose the ports we're interested in
EXPOSE 8080

# Set the default command to run on boot
# This will boot JBoss AS in the standalone mode and bind to all interface
CMD ["/opt/jboss/as/bin/run.sh", "-b", "0.0.0.0"]
