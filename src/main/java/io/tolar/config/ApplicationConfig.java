package io.tolar.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.ByteString;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import io.tolar.utils.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tolar.proto.Account.AddressBalance;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter exporter = new AutoJsonRpcServiceImplExporter();
        exporter.setErrorResolver(new TolarErrorResolver());
        exporter.setHttpStatusCodeProvider(new TolarHttpStatusCodeProvider());
        return exporter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("", "/swagger");
    }

    @Bean
    public Module dynamoDemoEntityDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ByteString.class, new ByteStringDeserializer());
        module.addSerializer(ByteString.class, new ByteStringSerializer());
        module.addSerializer(AddressBalance.class, new AddressBalanceSerializer());
        return module;
    }

}