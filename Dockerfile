FROM tomcat:10.1.26-jre21

WORKDIR /usr/local/tomcat

RUN mv webapps webapps_old && mv webapps.dist webapps && rm conf/tomcat-users.xml && rm webapps/manager/META-INF/context.xml

COPY ./build/libs/JR_mod4_FinalMentor-*.war webapps
COPY ./tomcat-users.xml conf
COPY ./context.xml webapps/manager/META-INF