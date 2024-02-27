
# Copyright 2019 Red Hat
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# ------------------------------------------------------------------------
#
# This is a Dockerfile for the jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8:7.4.15 image.

FROM registry.redhat.io/ubi8/ubi:latest

USER root

# Add scripts used to configure the image
COPY modules /tmp/scripts/

# Add all artifacts to the /tmp/artifacts directory
COPY \
    artifacts/jolokia-jvm-1.7.1.redhat-00001-agent.jar \
    artifacts/jmx_prometheus_javaagent-0.3.2.redhat-00005.jar \
    /tmp/artifacts/

# begin jboss.container.user:1.0

# Install required RPMs and ensure that the packages were installed
USER root
RUN yum --setopt=tsflags=nodocs install -y unzip tar rsync shadow-utils \
    && rpm -q unzip tar rsync shadow-utils

# Environment variables
ENV \
    HOME="/home/jboss" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.user/configure.sh" ]

# end jboss.container.user:1.0
# begin jboss.container.openjdk.jdk:11

# Install required RPMs and ensure that the packages were installed
USER root
RUN yum --setopt=tsflags=nodocs install -y java-11-openjdk-devel \
    && rpm -q java-11-openjdk-devel

# Environment variables
ENV \
    JAVA_HOME="/usr/lib/jvm/java-11" \
    JAVA_VENDOR="openjdk" \
    JAVA_VERSION="11" \
    JBOSS_CONTAINER_OPENJDK_JDK_MODULE="/opt/jboss/container/openjdk/jdk" 

# Labels
LABEL \
      org.jboss.product="openjdk"  \
      org.jboss.product.openjdk.version="11"  \
      org.jboss.product.version="11" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.openjdk.jdk/configure.sh" ]

# end jboss.container.openjdk.jdk:11
# begin jboss.container.java.jvm.api:1.0

# end jboss.container.java.jvm.api:1.0
# begin jboss.container.proxy.api:2.0

# end jboss.container.proxy.api:2.0
# begin jboss.container.java.proxy.bash:2.0

# Environment variables
ENV \
    JBOSS_CONTAINER_JAVA_PROXY_MODULE="/opt/jboss/container/java/proxy" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.java.proxy.bash/configure.sh" ]
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.java.proxy.bash/backward_compatibility.sh" ]

# end jboss.container.java.proxy.bash:2.0
# begin jboss.container.java.jvm.bash:1.0

# Environment variables
ENV \
    JBOSS_CONTAINER_JAVA_JVM_MODULE="/opt/jboss/container/java/jvm" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.java.jvm.bash/configure.sh" ]
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.java.jvm.bash/backward_compatibility.sh" ]

# end jboss.container.java.jvm.bash:1.0
# begin dynamic-resources:1.0

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/dynamic-resources/install.sh" ]

# end dynamic-resources:1.0
# begin jboss.container.jolokia:7

# Environment variables
ENV \
    AB_JOLOKIA_AUTH_OPENSHIFT="true" \
    AB_JOLOKIA_HTTPS="true" \
    AB_JOLOKIA_PASSWORD_RANDOM="true" \
    JBOSS_CONTAINER_JOLOKIA_MODULE="/opt/jboss/container/jolokia" \
    JOLOKIA_VERSION="1.7.1" 

# Labels
LABEL \
      io.fabric8.s2i.version.jolokia="1.7.1.redhat-00001" 

# Exposed ports
EXPOSE 8778
# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.jolokia/configure.sh" ]
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.jolokia/backward_compatibility.sh" ]

# end jboss.container.jolokia:7
# begin jboss.container.util.logging.bash:1.0

# Environment variables
ENV \
    JBOSS_CONTAINER_UTIL_LOGGING_MODULE="/opt/jboss/container/util/logging/" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.util.logging.bash/configure.sh" ]
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.util.logging.bash/backward_compatibility.sh" ]

# end jboss.container.util.logging.bash:1.0
# begin jboss.container.prometheus:7

# Environment variables
ENV \
    AB_PROMETHEUS_JMX_EXPORTER_CONFIG="/opt/jboss/container/prometheus/etc/jmx-exporter-config.yaml" \
    JBOSS_CONTAINER_PROMETHEUS_MODULE="/opt/jboss/container/prometheus" 

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.prometheus/configure.sh" ]
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.prometheus/backwards_compatibility.sh" ]

# end jboss.container.prometheus:7
# begin jboss.container.eap.prometheus.jmx-exporter-config:1.0

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/jboss.container.eap.prometheus.jmx-exporter-config/configure.sh" ]

# end jboss.container.eap.prometheus.jmx-exporter-config:1.0
# begin jboss.container.eap.prometheus.runtime:1.0

# end jboss.container.eap.prometheus.runtime:1.0
# begin os-eap-txnrecovery.bash:1.0

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/os-eap-txnrecovery.bash/install_as_root.sh" ]

# end os-eap-txnrecovery.bash:1.0
# begin os-eap-txnrecovery.run:python3

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/os-eap-txnrecovery.run/install_as_root.sh" ]

# end os-eap-txnrecovery.run:python3
# begin jboss.container.eap.tzdata-java:1.0

# Install required RPMs and ensure that the packages were installed
USER root
RUN yum --setopt=tsflags=nodocs install -y tzdata-java \
    && rpm -q tzdata-java

# end jboss.container.eap.tzdata-java:1.0
# begin os-eap-python:3.6

# Install required RPMs and ensure that the packages were installed
USER root
RUN yum --setopt=tsflags=nodocs install -y python36 \
    && rpm -q python36

# Custom scripts
USER root
RUN [ "bash", "-x", "/tmp/scripts/os-eap-python/configure.sh" ]

# end os-eap-python:3.6
# begin jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8:7.4.15

# Install required RPMs and ensure that the packages were installed
USER root
RUN yum --setopt=tsflags=nodocs install -y hostname python3-requests \
    && rpm -q hostname python3-requests

# Environment variables
ENV \
    AB_PROMETHEUS_JMX_EXPORTER_PORT="9799" \
    DEFAULT_ADMIN_USERNAME="eapadmin" \
    HTTPS_ENABLE_HTTP2="true" \
    JBOSS_HOME="/opt/eap" \
    JBOSS_IMAGE_NAME="jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8" \
    JBOSS_IMAGE_VERSION="7.4.15" \
    JBOSS_MODULES_SYSTEM_PKGS="jdk.nashorn.api" \
    LAUNCH_JBOSS_IN_BACKGROUND="true" \
    SSO_FORCE_LEGACY_SECURITY="true" 

# Labels
LABEL \
      com.redhat.component="jboss-eap-74-openjdk11-runtime-openshift-rhel8-container"  \
      com.redhat.deployments-dir="/opt/eap/standalone/deployments"  \
      description="The JBoss EAP 7.4 OpenJDK 11 runtime image"  \
      io.cekit.version="3.2.1"  \
      io.k8s.description="Base image to run an EAP server and application"  \
      io.k8s.display-name="JBoss EAP runtime image"  \
      io.openshift.expose-services="8080:http"  \
      io.openshift.tags="javaee,eap,eap7"  \
      maintainer="Red Hat"  \
      name="jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8"  \
      summary="The JBoss EAP 7.4 OpenJDK 11 runtime image"  \
      version="7.4.15" 

# Exposed ports
EXPOSE 8443 8080
# end jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8:7.4.15

USER root
RUN [ ! -d /tmp/scripts ] || rm -rf /tmp/scripts
RUN [ ! -d /tmp/artifacts ] || rm -rf /tmp/artifacts

# Clear package manager metadata
RUN yum clean all && [ ! -d /var/cache/yum ] || rm -rf /var/cache/yum



# Run user
USER 185

# Specify the working directory
WORKDIR /home/jboss
