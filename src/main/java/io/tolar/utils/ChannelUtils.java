package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.TolarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
public class ChannelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtils.class);
    private final int channelCount;
    private final int permitTimeout;

    private final TolarConfig tolarConfig;
    private final List<Channel> channelList;

    private final Semaphore available;
    private final Random random;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
        this.channelList = new ArrayList<>();
        this.random = new Random();
        this.available = new Semaphore(tolarConfig.getSemaphorePermitsAsInt(), true);
        //this.channelCount = tolarConfig.getChannelCountAsInt();
        this.permitTimeout = tolarConfig.getSemaphoreTimeoutAsInt();

        for (String host : tolarConfig.getHosts()) {
            channelList.add(generateChannel(host));
        }

        channelCount = tolarConfig.getHosts().size();
    }

    public Channel generateChannel(String host){
        return ManagedChannelBuilder
                .forAddress(host, tolarConfig.getPortAsInt())
                .usePlaintext()
                .build();
    }

    public Channel getChannel() {
        return channelList.get(random.nextInt(channelCount));
    }

    public void acquire(){
        try {
            available.tryAcquire(permitTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void release(){
        available.release();

        LOGGER.info("Available permits: " + available.availablePermits()
                + " queue length: " + available.getQueueLength());
    }
}
