package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.UrlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtils.class);

    @Autowired
    private UrlConfig urlConfig;

    public Channel getChannel() {
        LOGGER.info(urlConfig.getUrl() + " " + urlConfig.getPort());
        return ManagedChannelBuilder.forAddress(urlConfig.getUrl(), urlConfig.getPort())
                .usePlaintext()
                .build();
    }
}
