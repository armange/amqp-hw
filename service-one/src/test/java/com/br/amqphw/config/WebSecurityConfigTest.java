package com.br.amqphw.config;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;

public class WebSecurityConfigTest {
    
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void configure() {
        final WebSecurityConfig webSecurityConfig = new WebSecurityConfig();

        try {
            
            final CorsConfigurer<HttpSecurity> cors = mock(CorsConfigurer.class);
            final HttpSecurity http = mock(HttpSecurity.class);
            final CsrfConfigurer<HttpSecurity> csrfConfigurer = mock(CsrfConfigurer.class);
            final ExpressionInterceptUrlRegistry urlRegistry = mock(ExpressionInterceptUrlRegistry.class);
            final AuthorizedUrl anyRequest = mock(AuthorizedUrl.class);
            

            doReturn(cors).when(http).cors();
            doReturn(http).when(cors).and();
            doReturn(csrfConfigurer).when(http).csrf();
            doReturn(http).when(csrfConfigurer).disable();
            doReturn(urlRegistry).when(http).authorizeRequests();
            doReturn(anyRequest).when(urlRegistry).anyRequest();
            doReturn(urlRegistry).when(anyRequest).authenticated();
            doReturn(http).when(urlRegistry).and();

            webSecurityConfig.configure(http);

            verify(http).cors();
            verify(cors).and();
            verify(http).csrf();
            verify(csrfConfigurer).disable();
            verify(http).authorizeRequests();
            verify(urlRegistry).anyRequest();
            verify(anyRequest).authenticated();
            verify(urlRegistry).and();
            verify(http).httpBasic();
            
            verifyNoMoreInteractions(cors);
            verifyNoMoreInteractions(http);
            verifyNoMoreInteractions(csrfConfigurer);
            verifyNoMoreInteractions(urlRegistry);
            verifyNoMoreInteractions(anyRequest);
        } catch (final Exception e) {
            e.printStackTrace();

            fail(e);
        }
    }
}
