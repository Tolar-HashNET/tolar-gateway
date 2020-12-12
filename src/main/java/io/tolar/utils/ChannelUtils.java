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
    private final int permitTimeout;

    private final TolarConfig tolarConfig;
    private final List<Channel> channelList;
    private final Map<Channel, SemaphoreHolder> channelSemaphores;

    private final Random random;
    private final AtomicInteger roundRobinCounter;

    public ChannelUtils(TolarConfig tolarConfig) {
        this.tolarConfig = tolarConfig;
        ArrayList<Channel> tempList = new ArrayList<>();
        this.random = new Random();
        this.permitTimeout = tolarConfig.getSemaphoreTimeoutAsInt();

        HashMap<Channel, SemaphoreHolder> semaphoreTempMap = new HashMap<>();

        int index = 0;

        for (String host : tolarConfig.getHosts()) {
            Channel channel = generateChannel(host);
            tempList.add(channel);

            Semaphore semaphore = new Semaphore(tolarConfig.getSemaphorePermitsAsInt(), true);

            SemaphoreHolder semaphoreHolder = new SemaphoreHolder(semaphore, index, host);

            semaphoreTempMap.put(channel, semaphoreHolder);
            index++;
        }

        this.channelList = Collections.unmodifiableList(tempList);
        this.channelSemaphores = Collections.unmodifiableMap(semaphoreTempMap);
        this.roundRobinCounter = new AtomicInteger();
    }

    public Channel generateChannel(String host) {
        return ManagedChannelBuilder
                .forAddress(host, tolarConfig.getPortAsInt())
                .usePlaintext()
                .build();
    }

    public Channel getChannel(){
        return getChannel(null ,null);
    }

    public Channel getChannel(String address){
        return getChannel(address ,null);
    }

    public Channel getChannel(Channel channel){
        return getChannel(null,channel);
    }

    public Channel getChannel(String address, Channel channel) {
        Channel nextChannel = channel;

        if (nextChannel == null && address == null) {
            nextChannel = channelList.get(roundRobinCounter.getAndIncrement() % channelList.size());
        } else if(nextChannel == null & address != null){
            Character dragon = address.charAt(address.length() - 1);
            nextChannel = channelList.get(dragon % channelList.size());
        }

        Semaphore semaphore = channelSemaphores.get(nextChannel).getSemaphore();

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


        SemaphoreHolder semaphoreHolder = channelSemaphores.get(channel);

        Semaphore semaphore = semaphoreHolder.getSemaphore();
        semaphore.release();

        LOGGER.debug("Available permits: " + semaphore.availablePermits()
                + " queue length: " + semaphore.getQueueLength() + ", semaphore index: {}, host: {}",
                semaphoreHolder.getIndex(), semaphoreHolder.getHost());
    }

    public static class SemaphoreHolder {
        private final Semaphore semaphore;
        private final int index;
        private final String host;

        public SemaphoreHolder(Semaphore semaphore, int index, String host) {
            this.semaphore = semaphore;
            this.index = index;
            this.host = host;
        }

        public Semaphore getSemaphore() {
            return semaphore;
        }

        public int getIndex() {
            return index;
        }

        public String getHost() {
            return host;
        }
    }

}
