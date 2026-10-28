#
# 		Dockerfile for ECOOP 2027 Paper Artefact
#
# Creates a docker setup with:
# 	- Debian distribution
#	- Maven
#	- JDK 21
#
# Usage:
#	- Build: docker build -t jatymon .
#	- ABD: docker run --rm -i -v "$PWD/Benchmarks:/Benchmarks" jatymon bash -c "bash Scripts/run-orchestrator.sh ABD && bash Scripts/gen-benchmarks.sh"
#	- MultiPaxos: docker run --rm -i -v "$PWD/Benchmarks:/Benchmarks" jatymon bash -c "bash Scripts/run-orchestrator.sh MultiPaxos && bash Scripts/gen-benchmarks.sh"
#	- Both: docker run --rm -i -v "$PWD/Benchmarks:/Benchmarks" jatymon bash -c "bash Scripts/run-orchestrator.sh both && bash Scripts/gen-benchmarks.sh"
#
# Environment (pass with -e, e.g. -e MONITORS=off):
#	- REPLICAS 	(<int> default: 5)
#	- CLIENTS 	(<int> default: 1)
#	- FAULTS 	(<on, off> default: off)
#	- MONITORS 	(<on, off> default: on)
#

#
# Setup OS (Pinned)
#

FROM debian:bookworm@sha256:2c037a04925515fdd6ea85ea14a682d0e79931f5e9f5d07b6dbfc6ba12f9e858
ARG DEBIAN_SNAPSHOT=20261012T000000Z

# Freeze apt to a Debian snapshot
RUN rm -f /etc/apt/sources.list /etc/apt/sources.list.d/* && \
    printf '%s\n' \
      "Types: deb" \
      "URIs: http://snapshot.debian.org/archive/debian/${DEBIAN_SNAPSHOT}/" \
      "Suites: bookworm bookworm-updates" \
      "Components: main" \
      "Signed-By: /usr/share/keyrings/debian-archive-keyring.gpg" \
      "Check-Valid-Until: no" \
      "" \
      "Types: deb" \
      "URIs: http://snapshot.debian.org/archive/debian-security/${DEBIAN_SNAPSHOT}/" \
      "Suites: bookworm-security" \
      "Components: main" \
      "Signed-By: /usr/share/keyrings/debian-archive-keyring.gpg" \
      "Check-Valid-Until: no" \
      > /etc/apt/sources.list.d/snapshot.sources && \
    cat /etc/apt/sources.list.d/snapshot.sources

# curl + certificates for downloads, procps for ps (used by run.sh)
RUN apt-get update && \
    apt-get install -y --no-install-recommends ca-certificates curl procps && \
    rm -rf /var/lib/apt/lists/*
RUN dpkg-query -W -f='${Package}=${Version}\n' > /etc/installed-packages.txt

#
# Setup JDK 21 (Pinned)
#

ENV JAVA_HOME=/opt/java
RUN set -eux; \
    arch="$(dpkg --print-architecture)"; \
    case "$arch" in \
      amd64) \
        jdk_asset="OpenJDK21U-jdk_x64_linux_hotspot_21.0.12.1_1.tar.gz"; \
        jdk_sha256="ce79869e1307ed8ee1e2baa86a412b1eb5b75d10a01006d788a6f968bcfaee94"; \
        ;; \
      arm64) \
        jdk_asset="OpenJDK21U-jdk_aarch64_linux_hotspot_21.0.12.1_1.tar.gz"; \
        jdk_sha256="23e37e026f12f3e706f18938ff611db3032d075b09d0879a25d06718c773e223"; \
        ;; \ 
      *) \
        echo "unsupported architecture: $arch" >&2; \
        exit 1; \
        ;; \
    esac; \
    jdk_url="https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.12.1%2B1/${jdk_asset}"; \
    curl -fL --retry 3 "$jdk_url" -o /tmp/jdk.tar.gz; \
    echo "${jdk_sha256}  /tmp/jdk.tar.gz" | sha256sum -c -; \
    mkdir -p "$JAVA_HOME"; \
    tar -xzf /tmp/jdk.tar.gz -C "$JAVA_HOME" --strip-components=1 --no-same-owner; \
    rm /tmp/jdk.tar.gz

#
# Setup Maven (Pinned)
#

ENV MAVEN_HOME=/opt/maven
RUN set -eux; \
    maven_url="https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz"; \
    maven_sha512="a555254d6b53d267965a3404ecb14e53c3827c09c3b94b5678835887ab404556bfaf78dcfe03ba76fa2508649dca8531c74bca4d5846513522404d48e8c4ac8b"; \
    curl -fL --retry 3 "$maven_url" -o /tmp/maven.tar.gz; \
    echo "${maven_sha512}  /tmp/maven.tar.gz" | sha512sum -c -; \
    mkdir -p "$MAVEN_HOME"; \
    tar -xzf /tmp/maven.tar.gz -C "$MAVEN_HOME" --strip-components=1 --no-same-owner; \
    rm /tmp/maven.tar.gz

ENV PATH="${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
RUN java -version && \
    javac -version && \
    mvn -version

#
# Setup execution
#

WORKDIR /app

# Repository
COPY . .

RUN bash Scripts/build-tool.sh
RUN bash Scripts/gen-monitors.sh
RUN bash Scripts/build-usecases.sh

# Results are written here; mount a host folder to keep them
ENV BENCHMARKS=/Benchmarks
VOLUME /Benchmarks

CMD ["bash"]
