package com.cucumber_spring.demo.cucumber_spring_demo.features;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthStateService;
import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public abstract class Feature {
  protected final String apiBaseUri;
  protected final String apiBaseContextPath;
  protected final RequestSpecBuilder requestSpecBuilder;
  protected final ObjectMapper objectMapper;
  protected final AuthStateService authStateService;
  protected final ResponseCodeState responseCodeState;

  public Feature(
      final String apiBaseUri,
      final String apiBaseContextPath,
      final ObjectMapper objectMapper,
      final ContentType acceptContentType,
      final AuthStateService authStateService,
      final ResponseCodeState responseCodeState) {
    this.apiBaseUri = apiBaseUri;
    this.apiBaseContextPath = apiBaseContextPath;
    this.objectMapper = objectMapper;
    this.authStateService = authStateService;
    this.responseCodeState = responseCodeState;
    this.requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setBaseUri(apiBaseUri);
    requestSpecBuilder.setBasePath(apiBaseContextPath);
    requestSpecBuilder.setAccept(acceptContentType);
  }
}
