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
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
public class AuthorizationFilterTest {
    private static final String HEADER_NAME = "Authorization";
    private static final String API_TOKEN = "TOKEN";
    private static ServletRequest request;
    private static ServletResponse response;
    private static FilterChain chain;
    private static Filter authorizationFilter;

    @BeforeAll
    static void tearUp() throws ServletException, IOException {
        ApplicationConfiguration configuration = Mockito.mock(ApplicationConfiguration.class);
        Mockito.when(configuration.apiToken()).thenReturn(API_TOKEN);

        authorizationFilter = new AuthorizationFilter(configuration);

        request = Mockito.mock(ServletRequest.class, Mockito.withSettings().extraInterfaces(HttpServletRequest.class));
        response =
            Mockito.mock(ServletResponse.class, Mockito.withSettings().extraInterfaces(HttpServletResponse.class));

        chain = Mockito.mock(FilterChain.class);
        Mockito.doNothing().when(chain).doFilter(request, response);
    }

    @Test
    void givenNoActualToken_whenFiltering_thenExceptionIsThrown() throws IOException {
        Mockito.when(((HttpServletRequest) request).getHeader(HEADER_NAME)).thenReturn(null);
        Mockito.doThrow(new MissingOrIncorrectAuthorizationHeaderException()).when((HttpServletResponse) response)
            .sendError(HttpStatus.FORBIDDEN.value());

        assertThatExceptionOfType(MissingOrIncorrectAuthorizationHeaderException.class)
            .isThrownBy(() -> authorizationFilter.doFilter(request, response, chain));
    }

    @Test
    void givenIncorrectToken_whenFiltering_thenExceptionIsThrown() throws IOException {
        Mockito.when(((HttpServletRequest) request).getHeader(HEADER_NAME)).thenReturn("INCORRECT_TOKEN");
        Mockito.doThrow(new MissingOrIncorrectAuthorizationHeaderException()).when((HttpServletResponse) response)
            .sendError(HttpStatus.FORBIDDEN.value());

        assertThatExceptionOfType(MissingOrIncorrectAuthorizationHeaderException.class)
            .isThrownBy(() -> authorizationFilter.doFilter(request, response, chain));
    }

    @Test
    void givenActualToken_whenFiltering_thenNothingIsThrown() {
        Mockito.when(((HttpServletRequest) request).getHeader(HEADER_NAME)).thenReturn(API_TOKEN);

        assertThatNoException().isThrownBy(() -> authorizationFilter.doFilter(request, response, chain));
    }
}
