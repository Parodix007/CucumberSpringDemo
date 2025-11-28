package com.cucumber_spring.demo.cucumber_spring_demo.features.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.features.model.AuthUserDto;
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
  private static final String ACCESS_TOKEN_FIELD = "accessToken";
  private final String loginUrl;
  private final String meUrl;
  private AuthUserDto authUserDto;
  private int responseStatusCode;
  private JsonNode userMetadata;

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
    log.info("Created a user metadata {}", this.authUserDto);
  }

  @When("Make a request for JWT")
  public void makeARequestForJWT() throws JsonProcessingException {
    this.responseStatusCode =
        RestAssured.given()
            .spec(requestSpecBuilder.build())
            .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(authUserDto))
            .contentType(ContentType.JSON)
            .when()
            .post(loginUrl)
            .then()
            .extract()
            .statusCode();
  }

  @Then("Response should be {string}")
  public void responseShouldBe(final String expectedStatusCode) {
    assertEquals(Integer.valueOf(expectedStatusCode), responseStatusCode);
  }

  @When("Make a request for JWT and user metadata")
  public void makeARequestForJWTAndUserMetadata() throws JsonProcessingException {
    final RequestSpecification requestSpecification = requestSpecBuilder.build();
    final String jwtResponseString =
        RestAssured.given()
            .spec(requestSpecification)
            .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(authUserDto))
            .contentType(ContentType.JSON)
            .when()
            .post(loginUrl)
            .then()
            .extract()
            .asPrettyString();

    log.info("JWT response string {}", jwtResponseString);

    final JsonNode jwtResponseJsonNode = objectMapper.readTree(jwtResponseString);
    log.info("JWT response as json node {}", jwtResponseJsonNode);

    final String token = jwtResponseJsonNode.get(ACCESS_TOKEN_FIELD).asText();
    final String userMetadataJson =
        RestAssured.given()
            .spec(requestSpecification)
            .auth()
            .oauth2(token)
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
