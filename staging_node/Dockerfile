FROM ubuntu:20.10
RUN mkdir -p /home/thin_node/.tolar/keystore/staging_node/keys
RUN mkdir -p /home/thin_node/.tolar/keystore/staging_node/db
WORKDIR /home/thin_node/
COPY config.json .
COPY thin_node_bin .
RUN chmod +x ./thin_node_bin
CMD ["./thin_node_bin", "--config_path=/home/thin_node/config.json"]