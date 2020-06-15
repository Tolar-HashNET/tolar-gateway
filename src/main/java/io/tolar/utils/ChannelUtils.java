package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.TolarConfig;
import org.springframework.stereotype.Service;

@Service
public class ChannelUtils {
    private TolarConfig tolarConfig;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
    }

    public Channel getChannel() {
        return ManagedChannelBuilder
                .forAddress(tolarConfig.getHost(), tolarConfig.getPort())
                .usePlaintext()
                .build();
    }
}
