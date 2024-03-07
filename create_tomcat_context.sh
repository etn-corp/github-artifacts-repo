#!/bin/bash

echo "=> add a JNDI datasource in Tomcat"
sed -i -r 's/<\/Context>//' ${CATALINA_HOME}/conf/context.xml
echo "<Resource name=\"jdbc/tapDB\" auth=\"Container\" type=\"javax.sql.DataSource\"" >> ${CATALINA_HOME}/conf/context.xml
echo 'maxTotal="100" maxIdle="30" maxWaitMillis="10000"' >> ${CATALINA_HOME}/conf/context.xml
echo "username=\"OEMAPNEWUSER\" password=\"${DB_PASSWORD}\" driverClassName=\"oracle.jdbc.OracleDriver\"" >> ${CATALINA_HOME}/conf/context.xml
echo "url=\"${DB_URL}\"/>" >> ${CATALINA_HOME}/conf/context.xml
echo '</Context>' >> ${CATALINA_HOME}/conf/context.xml
echo "=> Done!"
