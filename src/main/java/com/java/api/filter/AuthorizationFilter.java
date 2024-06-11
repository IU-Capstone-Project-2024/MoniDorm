package com.java.api.filter;

import com.java.api.exception.MissingOrIncorrectAuthorizationHeaderException;
import com.java.configuration.ApplicationConfiguration;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class AuthorizationFilter implements Filter {
    private final String apiToken;

    public AuthorizationFilter(@NotNull ApplicationConfiguration configuration) {
        this.apiToken = configuration.apiToken();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var token = request.getHeader("Authorization");

        try {
            if (token == null || !token.equals(apiToken)) {
                throw new MissingOrIncorrectAuthorizationHeaderException();
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (MissingOrIncorrectAuthorizationHeaderException exception) {
            response.sendError(HttpStatus.FORBIDDEN.value());
        }
    }
}
