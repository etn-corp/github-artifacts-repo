#!/bin/bash

if [ ! -f /.tomcat_context_created ]; then
    /create_tomcat_context.sh
fi

exec ${CATALINA_HOME}/bin/catalina.sh run
