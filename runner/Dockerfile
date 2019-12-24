FROM ubuntu:18.04

# Install prerequisites
RUN apt-get update && apt-get install -y \
    curl \
    git \
  && rm -rf /var/lib/apt/lists/*

# Docker cli
ENV DOCKER_VERSION=19.03.5~3-0
ENV DOCKER_URL=https://download.docker.com/linux/ubuntu/dists/bionic/pool/stable/amd64/docker-ce-cli_${DOCKER_VERSION}~ubuntu-bionic_amd64.deb
RUN curl -o /tmp/docker-ce-cli.deb $DOCKER_URL \
  && dpkg -i /tmp/docker-ce-cli.deb \
  && rm /tmp/docker-ce-cli.deb

# Github runner
ENV RUNNER_VERSION=2.164.0
ENV RUNNER_URL=https://github.com/actions/runner/releases/download/v${RUNNER_VERSION}/actions-runner-linux-x64-${RUNNER_VERSION}.tar.gz
WORKDIR /app
RUN curl -0 -L -o /tmp/actions-runner.tar.gz $RUNNER_URL \
  && tar xzf /tmp/actions-runner.tar.gz \
  && rm /tmp/actions-runner.tar.gz
RUN bash /app/bin/installdependencies.sh 

# Prepare entrypoint
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh 

# setup user and permissions 
ARG uid=1000 
ARG gid=1000
ARG dgid=989
RUN groupadd --gid $gid github && groupadd --gid $dgid docker \
  && useradd --create-home --uid $uid --gid $gid --groups docker github 

# prepare workspace
ENV WORK_DIR=/workspace
WORKDIR $WORK_DIR
RUN chown -R github:github /app $WORK_DIR

USER github
ENTRYPOINT ["/app/entrypoint.sh"]