FROM gradle:6.7.1-jdk15 as builder
WORKDIR /build
COPY . .
RUN gradle -ParchiveFileName=jamcha.jar shadowJar

FROM openjdk:15-alpine3.12
WORKDIR /jamcha
COPY --from=builder /build/build/libs/jamcha.jar ./jamcha.jar
ENTRYPOINT [ "java", "-cp", "./jamcha.jar", "jp.co.tdc.jamcha.cmd.Main" ]
