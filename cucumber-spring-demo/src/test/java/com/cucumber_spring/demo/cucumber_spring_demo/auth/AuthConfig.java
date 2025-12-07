package com.cucumber_spring.demo.cucumber_spring_demo.auth;

import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
  @Bean
  @ScenarioScope
  AuthStateService authService(
      @Value("${api.base.url}") final String apiBaseUri,
      @Value("${api.auth.context.path}") final String apiBaseContextPath,
      @Value("${api.auth.login}") final String loginUrl) {
    return new AuthStateService(apiBaseUri, apiBaseContextPath, loginUrl);
  }
}
