#!/bin/bash

if [ -f /.tomcat_context_created ]; then
    echo "JNDI datasource already added in Tomcat context.xml"
    exit 0
fi


HOST=${MYSQL_HOST}
PORT=${MYSQL_PORT}
DATABASE=${MYSQL_DATABASE}
USER=${MYSQL_USER}
PASSWORD=${MYSQL_PASSWORD}

echo "=> add a JNDI datasource in Tomcat"
sed -i -r 's/<\/Context>//' ${CATALINA_HOME}/conf/context.xml
echo "<Resource name=\"jdbc/tapDB\" auth=\"Container\" type=\"javax.sql.DataSource\"" >> ${CATALINA_HOME}/conf/context.xml
echo 'maxTotal="100" maxIdle="30" maxWaitMillis="10000"' >> ${CATALINA_HOME}/conf/context.xml
echo "username=\"OEMAPNEWUSER\" password=\"sdfjhl758\" driverClassName=\"oracle.jdbc.OracleDriver\"" >> ${CATALINA_HOME}/conf/context.xml
echo "url=\"jdbc:oracle:thin:@egsmalld-lsnr-db.tcc.etn.com:1521:WTCRD810\"/>" >> ${CATALINA_HOME}/conf/context.xml
echo '</Context>' >> ${CATALINA_HOME}/conf/context.xml
echo "=> Done!"
touch /.tomcat_context_created
