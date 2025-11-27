package com.cucumber_spring.demo.cucumber_spring_demo.features.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.config.CommonsConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = {CommonsConfig.class})
@ActiveProfiles("test")
public class CucumberSpringConfiguration {}
