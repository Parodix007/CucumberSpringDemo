package com.cucumber_spring.demo.cucumber_spring_demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsConfig {
  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
