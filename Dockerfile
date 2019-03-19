FROM hseeberger/scala-sbt:8u181_2.12.8_1.2.8 as builder
COPY build.sbt /app/build.sbt
COPY project /app/project
COPY .scalafmt.conf /app/.scalafmt.conf
RUN awk 'BEGIN{for(v in ENVIRON) print v}'
WORKDIR /app
RUN sbt update test:update it:update
COPY . .
RUN sbt compile test stage

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/target/universal/stage /app
USER root
RUN useradd --system --create-home --uid 1001 --gid 0 emailservice && \
    chmod -R u=rX,g=rX /app && \
    chmod u+x,g+x /app/bin/emailservice && \
    chown -R 1001:root /app
USER 1001
ARG BUILD_VERSION
ENV VERSION=$BUILD_VERSION
EXPOSE 9000
ENTRYPOINT /app/bin/emailservice
CMD []
