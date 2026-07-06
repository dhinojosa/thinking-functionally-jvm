FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl gzip ca-certificates \
    && bash -lc 'source /usr/local/sdkman/bin/sdkman-init.sh && sdk install java 26-zulu && sdk default java 26-zulu && sdk install maven && sdk install gradle' \
    && curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz \
      | gzip -d > /usr/local/bin/cs \
    && chmod +x /usr/local/bin/cs \
    && cs install --install-dir /usr/local/bin sbt scala scalac \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
