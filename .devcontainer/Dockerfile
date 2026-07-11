FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

RUN bash -lc 'source /usr/local/sdkman/bin/sdkman-init.sh && sdk install java 21.0.4-zulu && sdk install java 26-zulu && sdk default java 26-zulu && sdk install maven && sdk install gradle' \
    && curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz \
      | gzip -d > /usr/local/bin/cs \
    && chmod +x /usr/local/bin/cs

USER vscode

RUN mkdir -p /home/vscode/.local/bin \
    && cs install --install-dir /home/vscode/.local/bin sbt scala scalac
