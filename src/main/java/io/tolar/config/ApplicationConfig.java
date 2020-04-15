package io.tolar.config;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import io.tolar.utils.TolarErrorResolver;
import io.tolar.utils.TolarHttpStatusCodeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

}