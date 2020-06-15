package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.TolarConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChannelUtils {
    private static final int CHANNEL_NUMBER = 10;
    private TolarConfig tolarConfig;
    private final List<Channel> channelList;
    private final Random random;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
        channelList = new ArrayList<>();
        random = new Random();
        for(int i = 0; i < CHANNEL_NUMBER; i++){
            channelList.add(generateChannel());
        }

    }

    public Channel generateChannel(){
        return ManagedChannelBuilder
                .forAddress(tolarConfig.getHost(), tolarConfig.getPort())
                .usePlaintext()
                .build();
    }

    public Channel getChannel() {
        return channelList.get(random.nextInt(CHANNEL_NUMBER));
    }
}
