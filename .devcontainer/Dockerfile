FROM mcr.microsoft.com/devcontainers/java:1-26-bookworm

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl gzip ca-certificates \
    && curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz \
      | gzip -d > /usr/local/bin/cs \
    && chmod +x /usr/local/bin/cs \
    && cs install --install-dir /usr/local/bin sbt scala scalac \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
