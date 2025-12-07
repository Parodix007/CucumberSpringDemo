package com.cucumber_spring.demo.cucumber_spring_demo.config;

import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.ScenarioScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfig {
  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  @ScenarioScope
  ResponseCodeState responseCodeState() {
    return new ResponseCodeState();
  }
}
