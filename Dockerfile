FROM ubuntu:14.04
MAINTAINER Vladimir Iakovlev <nvbn.rm@gmail.com>

RUN adduser --disabled-password --gecos "" subman

ENV "VERSION" 2015_01_19_21_54

RUN apt-get update -yqq
RUN apt-get upgrade -yqq
RUN apt-get install openjdk-7-jdk curl git -yqq --no-install-recommends
RUN curl -s https://raw.githubusercontent.com/technomancy/leiningen/2.5.0/bin/lein > /usr/local/bin/lein
RUN chmod 0755 /usr/local/bin/lein

WORKDIR /home/subman
COPY . /home/subman/code
RUN chown -R subman code
USER subman
WORKDIR /home/subman/code

RUN lein uberjar

CMD java -jar target/subman-parser-*-SNAPSHOT-standalone.jar