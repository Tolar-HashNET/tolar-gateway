package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.TolarConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class ChannelUtils {
    private TolarConfig tolarConfig;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
    }

    @Bean
    public Channel getChannel() {
        return ManagedChannelBuilder
                .forAddress(tolarConfig.getHost(), tolarConfig.getPort())
                .usePlaintext()
                .build();
    }
}
