FROM ubuntu:18.04
RUN mkdir -p /home/thin_node/.tolar/keystore/Thin_node/keys
WORKDIR /home/thin_node/
COPY config.json .
COPY tolar_daemon_linux .
CMD ["./tolar_daemon_linux", "--config_path=/home/thin_node/config.json"]