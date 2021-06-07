FROM gradle:3.5-jdk9
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle $APP_HOME
  
COPY src $APP_HOME/src
COPY gradle $APP_HOME/gradle
COPY gradlew $APP_HOME
USER root
RUN $APP_HOME/gradlew build -x test --info --stacktrace

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait
    
EXPOSE 8989
CMD /wait &&  $APP_HOME/gradlew bootRun
