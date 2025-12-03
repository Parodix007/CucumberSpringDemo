package com.cucumber_spring.demo.cucumber_spring_demo.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AuthConfig {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    AuthService authService(@Value("${api.base.url}") final String apiBaseUri,
                            @Value("${api.auth.context.path}") final String apiBaseContextPath,
                            @Value("${api.auth.login}") final String loginUrl) {
        return new AuthService(apiBaseUri, apiBaseContextPath, loginUrl);
    }
}
