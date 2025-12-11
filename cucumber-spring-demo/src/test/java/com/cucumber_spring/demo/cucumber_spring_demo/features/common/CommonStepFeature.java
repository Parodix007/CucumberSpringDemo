package com.cucumber_spring.demo.cucumber_spring_demo.features.common;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthStateService;
import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserDto;
import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonStepFeature {
  private final ResponseCodeState responseCodeState;
  private final AuthStateService authStateService;
  private final ObjectMapper objectMapper;

  CommonStepFeature(
      final ResponseCodeState responseCodeState,
      final AuthStateService authStateService,
      final ObjectMapper objectMapper) {
    this.responseCodeState = responseCodeState;
    this.authStateService = authStateService;
    this.objectMapper = objectMapper;
  }

  @Given("Authorize a user with")
  public void authorizeAUserWith(final AuthUserDto authUserDto) {
    authStateService.createDataForAuth(authUserDto);
    authStateService.makeARequestForJwt();
  }

  @Then("Response should be {string}")
  public void responseShouldBe(final String responseCode) {
    assertEquals(responseCode, String.valueOf(responseCodeState.getResponseCode()));
  }

  @Then("Response should be {int}")
  public void responseShouldBe(final int responseCode) {
    assertEquals(responseCode, responseCodeState.getResponseCode());
  }

  @DataTableType
  public AuthUserDto authUserDto(final Map<String, String> userMetadata) {
    return objectMapper.convertValue(userMetadata, AuthUserDto.class);
  }
}
