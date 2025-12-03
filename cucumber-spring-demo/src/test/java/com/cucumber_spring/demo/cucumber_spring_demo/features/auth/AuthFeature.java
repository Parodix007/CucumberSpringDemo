package com.cucumber_spring.demo.cucumber_spring_demo.features.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthService;
import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
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
  private AuthService authService;

  public AuthFeature(
      @Value("${api.base.url}") final String apiBaseUri,
      @Value("${api.auth.context.path}") final String apiBaseContextPath,
      @Value("${api.auth.me}") final String meUrl,
      final ObjectMapper objectMapper,
      final AuthService authService) {
    super(apiBaseUri, apiBaseContextPath, objectMapper, ContentType.JSON);
    this.meUrl = meUrl;
    this.authService = authService;
  }

  @Given("Create a user data using {string} and {string}")
  public void createAUserDataUsingAnd(final String username, final String password) {
    authService.createDataForAuth(username, password);
  }

  @When("Make a request for JWT")
  public void makeARequestForJWT() {
    authService.makeARequestForJwt();
  }

  @When("Make a request for JWT and user metadata")
  public void makeARequestForJWTAndUserMetadata() throws JsonProcessingException {
    final String userMetadataJson =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authService.getUserJwt())
            .when()
            .get(meUrl)
            .then()
            .extract()
            .asPrettyString();

    log.info("User metadata as string {}", userMetadataJson);

    userMetadata = objectMapper.readTree(userMetadataJson);

    log.info("user metadata as Json node {}", userMetadata);
  }

  @Then("Response should contains {string} and {string} and {string}")
  public void responseShouldContainsAndAnd(
      final String username, final String password, final String id) {
    assertEquals(userMetadata.get(USERNAME_FIELD).asText(""), username);
    assertEquals(userMetadata.get(PASSWORD_FIELD).asText(""), password);
    assertEquals(userMetadata.get(ID_FIELD).asText(""), id);
  }
}
