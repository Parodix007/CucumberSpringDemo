package com.cucumber_spring.demo.cucumber_spring_demo.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public abstract class Feature {
  protected final String apiBaseUri;
  protected final String apiBaseContextPath;
  protected final RequestSpecBuilder requestSpecBuilder;
  protected final ObjectMapper objectMapper;

  public Feature(
      final String apiBaseUri,
      final String apiBaseContextPath,
      final ObjectMapper objectMapper,
      final ContentType acceptContentType) {
    this.apiBaseUri = apiBaseUri;
    this.apiBaseContextPath = apiBaseContextPath;
    this.objectMapper = objectMapper;
    this.requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setBaseUri(apiBaseUri);
    requestSpecBuilder.setBasePath(apiBaseContextPath);
    requestSpecBuilder.setAccept(acceptContentType);
  }
}
