package io.tolar.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tolar-hashnet")
public class TolarConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarConfig.class);
    private static final String DEFAULT_HOST = "127.0.0.1";
    private String host;
    private String port;
    private String channelCount;
    private String semaphorePermits;
    private String semaphoreTimeout;

    public String getHost() {
        if ("${TOLAR_HASHNET_HOST}".equals(host)) {
            LOGGER.info("TOLAR_HASHNET_HOST variable not found! Defaulting to {}", DEFAULT_HOST);
            return DEFAULT_HOST;
        }

        return host;
    }

    public int getPortAsInt() {
        return getAsInt(port, "TOLAR_HASHNET_PORT", 9200);
    }

    public int getChannelCountAsInt() {
        return getAsInt(channelCount, "TOLAR_CHANNEL_COUNT", 10);
    }

    public int getSemaphorePermitsAsInt() {
        return getAsInt(semaphorePermits, "TOLAR_SEMAPHORE_PERMITS", 10);
    }

    public int getSemaphoreTimeoutAsInt() {
        return getAsInt(semaphoreTimeout, "TOLAR_SEMAPHORE_TIMEOUT", 60);
    }

    private int getAsInt(String variableValue, String variableName, int defaultValue) {
        if ( ("${" + variableName + "}").equals(variableValue)) {
            LOGGER.info("{} variable not found! Defaulting to {}", variableName, defaultValue);
            return defaultValue;
        }

        try {
            return Integer.parseInt(variableValue);
        } catch (NumberFormatException notNumber) {
            LOGGER.error("{} is not a number! Defaulting to {}", variableName, defaultValue);
        }

        return defaultValue;
    }

    public String getChannelCount() {
        return channelCount;
    }

    public String getPort() {
        return port;
    }

    public void setChannelCount(String channelCount) {
        this.channelCount = channelCount;
    }

    public String getSemaphorePermits() {
        return semaphorePermits;
    }

    public void setSemaphorePermits(String semaphorePermits) {
        this.semaphorePermits = semaphorePermits;
    }

    public String getSemaphoreTimeout() {
        return semaphoreTimeout;
    }

    public void setSemaphoreTimeout(String semaphoreTimeout) {
        this.semaphoreTimeout = semaphoreTimeout;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
