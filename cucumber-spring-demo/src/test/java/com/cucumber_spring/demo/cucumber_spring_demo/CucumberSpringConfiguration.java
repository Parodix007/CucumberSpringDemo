package com.cucumber_spring.demo.cucumber_spring_demo;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthConfig;
import com.cucumber_spring.demo.cucumber_spring_demo.config.CommonsConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = {CommonsConfig.class, AuthConfig.class})
@ActiveProfiles("test")
public class CucumberSpringConfiguration {}
