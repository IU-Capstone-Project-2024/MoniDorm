package com.java;

import com.java.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfiguration.class)
public class MonidormApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonidormApplication.class);
    }
}
