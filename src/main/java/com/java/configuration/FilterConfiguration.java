package com.java.configuration;

import com.java.api.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class FilterConfiguration {
    private final ApplicationConfiguration configuration;

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationFilter(configuration));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
