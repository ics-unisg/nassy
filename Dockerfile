FROM gradle:3.5-jdk9 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle $APP_HOME
  
COPY src $APP_HOME/src
COPY gradle $APP_HOME/gradle
#COPY dbSchema $APP_HOME/dbSchema
#COPY dataForTesting $APP_HOME/dataForTesting
#COPY TestForCheetah $APP_HOME/TestForCheetah
COPY gradlew $APP_HOME
#COPY --chown=gradle:gradle . $APP_HOME/src
USER root
#RUN chown -R gradle $APP_HOME/src
RUN $APP_HOME/gradlew build -x test --info --stacktrace

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait
    
EXPOSE 8989
CMD /wait &&  $APP_HOME/gradlew bootRun
