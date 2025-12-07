package com.cucumber_spring.demo.cucumber_spring_demo.features.common;

import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonStepFeature {
  private final ResponseCodeState responseCodeState;

  CommonStepFeature(final ResponseCodeState responseCodeState) {
    this.responseCodeState = responseCodeState;
  }

  @Then("Response should be {string}")
  public void responseShouldBe(final String responseCode) {
    assertEquals(responseCode, String.valueOf(responseCodeState.getResponseCode()));
  }

  @Then("Response should be {int}")
  public void responseShouldBe(final int responseCode) {
    assertEquals(responseCode, responseCodeState.getResponseCode());
  }
}
