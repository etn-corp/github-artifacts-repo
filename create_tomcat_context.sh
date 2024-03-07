#!/bin/bash

if [ -f /tomcat_context_created ]; then
    echo "JNDI datasource already added in Tomcat context.xml"
    exit 0
fi

echo "=> add a JNDI datasource in Tomcat"
echo "1=> ${DB_URL}"
echo "2=> ${DB_PASSWORD}"
sed -i -r 's/<\/Context>//' ${CATALINA_HOME}/conf/context.xml
echo "<Resource name=\"jdbc/tapDB\" auth=\"Container\" type=\"javax.sql.DataSource\"" >> ${CATALINA_HOME}/conf/context.xml
echo 'maxTotal="100" maxIdle="30" maxWaitMillis="10000"' >> ${CATALINA_HOME}/conf/context.xml
echo "username=\"OEMAPNEWUSER\" password=\"${DB_PASSWORD}\" driverClassName=\"oracle.jdbc.OracleDriver\"" >> ${CATALINA_HOME}/conf/context.xml
echo "url=\"${DB_URL}\"/>" >> ${CATALINA_HOME}/conf/context.xml
echo '</Context>' >> ${CATALINA_HOME}/conf/context.xml
echo "=> Done!"
touch /tomcat_context_created
