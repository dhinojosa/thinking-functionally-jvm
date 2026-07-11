FROM mcr.microsoft.com/devcontainers/base:ubuntu-24.04

ARG TARGETARCH
ARG JDK_21_VERSION=21.0.11%2B10
ARG JDK_26_VERSION=26.0.1%2B8
ARG MAVEN_VERSION=3.9.15
ARG GRADLE_VERSION=9.6.0
ARG SBT_VERSION=1.9.9

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl gzip tar unzip \
    && rm -rf /var/lib/apt/lists/*

RUN case "${TARGETARCH}" in \
      amd64) JDK_ARCH=x64 ;; \
      arm64) JDK_ARCH=aarch64 ;; \
      *) echo "Unsupported architecture: ${TARGETARCH}" >&2; exit 1 ;; \
    esac \
    && mkdir -p /opt/java/jdk-21 /opt/java/jdk-26 \
    && curl -fsSL "https://api.adoptium.net/v3/binary/version/jdk-${JDK_21_VERSION}/linux/${JDK_ARCH}/jdk/hotspot/normal/eclipse" \
      | tar -xz -C /opt/java/jdk-21 --strip-components=1 \
    && curl -fsSL "https://api.adoptium.net/v3/binary/version/jdk-${JDK_26_VERSION}/linux/${JDK_ARCH}/jdk/hotspot/normal/eclipse" \
      | tar -xz -C /opt/java/jdk-26 --strip-components=1

RUN curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz" \
      | tar -xz -C /opt \
    && mv "/opt/apache-maven-${MAVEN_VERSION}" /opt/maven \
    && curl -fsSL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o /tmp/gradle.zip \
    && unzip -q /tmp/gradle.zip -d /opt \
    && mv "/opt/gradle-${GRADLE_VERSION}" /opt/gradle \
    && curl -fsSL "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" \
      | tar -xz -C /opt \
    && rm /tmp/gradle.zip

ENV JAVA_HOME=/opt/java/jdk-26
ENV PATH="/opt/java/jdk-26/bin:/opt/maven/bin:/opt/gradle/bin:/opt/sbt/bin:${PATH}"

USER vscode
