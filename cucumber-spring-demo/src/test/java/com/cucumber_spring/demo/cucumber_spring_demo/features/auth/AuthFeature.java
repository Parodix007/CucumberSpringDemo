package com.cucumber_spring.demo.cucumber_spring_demo.features.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.features.model.AuthUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthFeature extends Feature {
  private final String loginUrl;
  private final String meUrl;
  private AuthUserDto authUserDto;
  private int responseStatusCode;

  public AuthFeature(
      @Value("${api.base.url}") final String apiBaseUri,
      @Value("${api.auth.context.path}") final String apiBaseContextPath,
      @Value("${api.auth.login}") final String loginUrl,
      @Value("${api.auth.me}") final String meUrl,
      final ObjectMapper objectMapper) {
    super(apiBaseUri, apiBaseContextPath, objectMapper, ContentType.JSON);
    this.loginUrl = loginUrl;
    this.meUrl = meUrl;
  }

  @Given("Create a user data using {string} and {string}")
  public void createAUserDataUsingAnd(final String username, final String password) {
    this.authUserDto = new AuthUserDto(username, password);
  }

  @When("Make a request for JWT")
  public void makeARequestForJWT() throws JsonProcessingException {
    this.responseStatusCode =
        RestAssured.given()
            .spec(requestSpecBuilder.build())
            .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(authUserDto))
            .contentType(ContentType.JSON)
            .post(loginUrl)
            .then()
            .extract()
            .statusCode();
  }

  @Then("Response should be {string}")
  public void responseShouldBe(final String expectedStatusCode) {
    assertEquals(Integer.valueOf(expectedStatusCode), responseStatusCode);
  }

  @Then("Response should contains {string} and {string} and {string}")
  public void responseShouldContainsAndAnd(
      final String username, final String password, final String id) {

  }
}
