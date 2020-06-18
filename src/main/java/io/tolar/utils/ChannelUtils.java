package io.tolar.utils;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.tolar.config.TolarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChannelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtils.class);
    private final int channelCount;
    private final int permitTimeout;

    private final TolarConfig tolarConfig;
    private final List<Channel> channelList;
    private Map<Channel, Semaphore> channelSemaphores;

    private final Semaphore available;
    private final Random random;
    private final AtomicInteger roundRobinCounter;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
        this.channelList = new ArrayList<>();
        this.random = new Random();
        this.available = new Semaphore(tolarConfig.getSemaphorePermitsAsInt(), true);
        //this.channelCount = tolarConfig.getChannelCountAsInt();
        this.permitTimeout = tolarConfig.getSemaphoreTimeoutAsInt();

        HashMap<Channel, Semaphore> semaphoreTempMap = new HashMap<>();

        for (String host : tolarConfig.getHosts()) {
            Channel channel = generateChannel(host);
            channelList.add(channel);
            semaphoreTempMap.put(channel, new Semaphore(tolarConfig.getSemaphorePermitsAsInt(), true));
        }

        this.channelSemaphores = Collections.unmodifiableMap(semaphoreTempMap);
        this.roundRobinCounter = new AtomicInteger();

        channelCount = tolarConfig.getHosts().size();
    }

    public Channel generateChannel(String host) {
        return ManagedChannelBuilder
                .forAddress(host, tolarConfig.getPortAsInt())
                .usePlaintext()
                .build();
    }

    public Channel getChannel() {
        Channel nextChannel = channelList.get(roundRobinCounter.getAndIncrement());
        Semaphore semaphore = channelSemaphores.get(nextChannel);

        if (semaphore.availablePermits() < 1) {
            try {
                Thread.sleep(10 + random.nextInt(490));
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
                Thread.currentThread().interrupt();
            }

            return getChannel();
        }

        acquire(semaphore);
        return nextChannel;
    }

    public void acquire(Semaphore semaphore) {
        try {
            semaphore.tryAcquire(permitTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void release(Channel channel) {
        if (channel == null) {
            return;
        }


        Semaphore semaphore = channelSemaphores.get(channel);

        semaphore.release();

        LOGGER.info("Available permits: " + semaphore.availablePermits()
                + " queue length: " + semaphore.getQueueLength());
    }

}
