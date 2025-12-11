package com.cucumber_spring.demo.cucumber_spring_demo.features.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthStateService;
import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class AuthFeature extends Feature {
  private static final String USERNAME_FIELD = "username";
  private static final String PASSWORD_FIELD = "password";
  private static final String ID_FIELD = "id";
  private final String meUrl;
  private JsonNode userMetadata;

  public AuthFeature(
      @Value("${api.base.url}") final String apiBaseUri,
      @Value("${api.auth.context.path}") final String apiBaseContextPath,
      @Value("${api.auth.me}") final String meUrl,
      final ObjectMapper objectMapper,
      final AuthStateService authService,
      final ResponseCodeState responseCodeState) {
    super(
        apiBaseUri,
        apiBaseContextPath,
        objectMapper,
        ContentType.JSON,
        authService,
        responseCodeState);
    this.meUrl = meUrl;
  }

  @Given("Create a user data using {string} and {string}")
  public void createAUserDataUsingAnd(final String username, final String password) {
    authStateService.createDataForAuth(username, password);
  }

  @When("Make a request for JWT")
  public void makeARequestForJWT() {
    authStateService.makeARequestForJwt();
    responseCodeState.setResponseCode(authStateService.getStatusCode());
  }

  @When("Make a request for user metadata")
  public void makeARequestForJWTAndUserMetadata() throws JsonProcessingException {
    final ExtractableResponse<Response> userMetadataResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .get(meUrl)
            .then()
            .extract();

    final String userMetadataJson = userMetadataResponse.asPrettyString();

    log.info("User metadata as string {}", userMetadataJson);

    userMetadata = objectMapper.readTree(userMetadataJson);

    log.info("user metadata as Json node {}", userMetadata);

    responseCodeState.setResponseCode(userMetadataResponse.statusCode());
  }

  @Then("Response should contains {string} and {string} and {string}")
  public void responseShouldContainsAndAnd(
      final String username, final String password, final String id) {
    assertEquals(userMetadata.get(USERNAME_FIELD).asText(""), username);
    assertEquals(userMetadata.get(PASSWORD_FIELD).asText(""), password);
    assertEquals(userMetadata.get(ID_FIELD).asText(""), id);
  }
}
