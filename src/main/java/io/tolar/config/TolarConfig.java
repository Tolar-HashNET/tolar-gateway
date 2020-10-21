package io.tolar.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tolar-hashnet")
public class TolarConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarConfig.class);
    private static final String DEFAULT_HOST = "127.0.0.1";
    private String hosts;
    private String port;
    private String semaphorePermits;
    private String semaphoreTimeout;

    public List<String> getHosts() {
        if ("${TOLAR_HASHNET_HOSTS}".equals(hosts)) {
            LOGGER.info("TOLAR_HASHNET_HOST variable not found! Defaulting to {}", DEFAULT_HOST);
            return Collections.singletonList(DEFAULT_HOST);
        }

        return Arrays.asList(hosts.split(","));
    }

    public int getPortAsInt() {
        return getAsInt(port, "TOLAR_HASHNET_PORT", 9200);
    }

    public int getSemaphorePermitsAsInt() {
        return getAsInt(semaphorePermits, "TOLAR_SEMAPHORE_PERMITS", 100);
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

    public String getPort() {
        return port;
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

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

}
