FROM openjdk:8
RUN apt-get update
RUN apt-get install -y netcat
RUN apt-get install -y jq
RUN apt-get install -y telnet

ENV SCALA_VERSION 2.12.2
ENV SBT_VERSION 1.1.6

RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

WORKDIR /paymentservice
ADD . /paymentservice
RUN sbt assembly
#COPY start.sh /usr/bin/start.sh
CMD ["/bin/bash","-c","chmod +x /paymentservice/start.sh && /paymentservice/start.sh"]
