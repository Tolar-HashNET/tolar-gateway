FROM ubuntu:latest

# We need this to avoid interactive prompts during installation of certain packages
ARG DEBIAN_FRONTEND="noninteractive"

# Environment variables needed in the container
ENV TZ=Europe/Zagreb \
    HOME=/home/coder \
    SHELL=/bin/bash

# System packages
RUN apt update -y && apt upgrade -y && apt install -y \
    curl \
    dnsutils \
    expect \
    git \
    htop \
    iputils-ping \
    jq \
    maven \
    netcat \
    net-tools \
    openjdk-8-jdk-headless \
    python3-pip \
    software-properties-common \
    sshpass \
    sudo \
    telnet \
    unzip \
    vim \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Set default java version
RUN update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

# Add user coder and give sudo rights
RUN adduser --gecos '' --disabled-password coder && \
    echo "coder ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/nopasswd

# su - coder 
USER coder

# VSCode extensions will be cached in a named volume 
# for faster rebuilds 
RUN mkdir -p \
    ${HOME}/.vscode-server/extensions \
    ${HOME}/.vscode-server-insiders/extensions

# Copy scripts needed for setting up the container
RUN mkdir -p ${HOME}/deps
COPY --chown=coder:coder .devcontainer/scripts ${HOME}/deps/

# Setup Message of the day
RUN sudo chmod -x /etc/update-motd.d/[1-9]* \
    && sudo cp ${HOME}/deps/motd /etc/motd \
    && echo "cat /etc/motd" >> $HOME/.bashrc \
    && echo "cat /etc/motd" >> $HOME/.profile

# Setup aliases and functions
RUN cat ${HOME}/deps/aliases >> ${HOME}/.bashrc \
    && cat ${HOME}/deps/aliases >> ${HOME}/.profile

# Cleanup
RUN rm -rf ${HOME}/deps

WORKDIR ${HOME}/workspace

# Dummy command to keep the container from exiting
CMD tail -f /dev/null
