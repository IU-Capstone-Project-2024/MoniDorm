package com.java.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebCORSConfiguration {
    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
        final int maxAgeValue = 3600;

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080", "http://localhost:80")
                    .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")
                    .maxAge(maxAgeValue)
                    .allowedHeaders("Requestor-Type")
                    .exposedHeaders("X-Get-Header");
            }
        };
    }
}
