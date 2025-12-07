package com.cucumber_spring.demo.cucumber_spring_demo.auth;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserDto;
import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserResDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthStateService {
  private final RequestSpecification requestSpecification;
  private final String loginUrl;
  private AuthUserDto authUserDto;
  private AuthUserResDto authUserResDto;
  private int statusCode;

  AuthStateService(final String baseUrl, final String contextPath, final String loginUrl) {
    final RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setBaseUri(baseUrl);
    requestSpecBuilder.setBasePath(contextPath);
    requestSpecBuilder.setAccept(ContentType.JSON);
    this.requestSpecification = requestSpecBuilder.build();
    this.loginUrl = loginUrl;
  }

  public void createDataForAuth(final String username, final String password) {
    this.authUserDto = new AuthUserDto(username, password);

    log.info("Created a user metadata {}", this.authUserDto);
  }

  public void createDataForAuth(final AuthUserDto authUserDto) {
    this.authUserDto = authUserDto;

    log.info("Created a user metadata {}", this.authUserDto);
  }

  public void makeARequestForJwt() {
    final ExtractableResponse<Response> extract =
        RestAssured.given()
            .spec(requestSpecification)
            .body(authUserDto)
            .contentType(ContentType.JSON)
            .when()
            .post(loginUrl)
            .then()
            .extract();
    log.info("Authorization response {}", extract);
    this.statusCode = extract.statusCode();

    if (this.statusCode == 200) {
      this.authUserResDto = extract.body().as(AuthUserResDto.class);
      log.info("Authorization user response {}", authUserResDto);
      return;
    }

    log.warn("Authorization response is not success!");
  }

  public String getUserJwt() {
    return this.authUserResDto.getAccessToken();
  }

  public int getStatusCode() {
    return statusCode;
  }
}
