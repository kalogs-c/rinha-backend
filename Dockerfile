FROM clojure:lein AS build

WORKDIR /app

COPY project.clj /app

RUN lein deps

COPY . /app

RUN lein uberjar

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/uberjar/*-standalone.jar ./app.jar

EXPOSE  80

ENTRYPOINT [ "java", "-jar", "app.jar" ]
