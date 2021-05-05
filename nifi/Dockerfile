FROM apache/nifi:1.13.0
LABEL maintainer="Martin Eigenmann <martin.eigenmann@unisg.ch>"

USER root

RUN apt-get update 

# Install Python3
RUN apt-get install -y python3 python3-pip


RUN pip3 install requests
RUN pip3 install termcolor
RUN pip3 install pandas
RUN pip3 install scipy
RUN pip3 install mysql-connector-python
RUN pip3 install statistics
RUN pip3 install numpy
RUN pip3 install requests
RUN pip3 install torch