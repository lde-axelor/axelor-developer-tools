ARG AXELOR_VERSION=5.4.11

FROM tomcat:8.5-jdk8 as base

ARG AXELOR_VERSION
WORKDIR /work

ADD https://github.com/axelor/open-suite-webapp/archive/refs/tags/v$AXELOR_VERSION.tar.gz ./webapp.tar.gz
RUN tar xf ./webapp.tar.gz -C ./ --strip-component=1 && rm -rf ./webapp.tar.gz
COPY . ./modules/axelor-developer-tools

FROM base as test
RUN ./gradlew test
