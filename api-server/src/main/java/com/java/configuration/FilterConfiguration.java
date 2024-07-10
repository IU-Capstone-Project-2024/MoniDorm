package com.java.configuration;

import com.java.api.filter.AuthorizationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class FilterConfiguration {
    private final ApplicationConfiguration configuration;

    @Bean
    public FilterRegistrationBean<AuthorizationTokenFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationTokenFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationTokenFilter(configuration));
        registrationBean.addUrlPatterns("/api/temporary_disabled");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
