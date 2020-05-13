package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.UrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelUtils {
    @Autowired
    private UrlConfig urlConfig;

    public Channel getChannel() {
        return ManagedChannelBuilder
                .forAddress(urlConfig.getUrl(), urlConfig.getPort())
                .usePlaintext()
                .build();
    }
}
