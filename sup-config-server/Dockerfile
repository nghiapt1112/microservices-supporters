FROM openjdk:8u162-jdk
MAINTAINER Nghia.Pham<tuannghia.sun@gmail.com>

ADD ./build/libs/*.jar app.jar

# Regarding settings of java.security.egd, see http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
