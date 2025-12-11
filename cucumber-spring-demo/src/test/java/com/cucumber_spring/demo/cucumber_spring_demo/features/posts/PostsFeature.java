package com.cucumber_spring.demo.cucumber_spring_demo.features.posts;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthStateService;
import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.features.posts.model.PostDto;
import com.cucumber_spring.demo.cucumber_spring_demo.features.posts.model.PaginationPostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DocStringType;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PostsFeature extends Feature {
  private final String addUrl;
  private final String userUrl;
  private PostDto postDto;
  private PostDto postDtoResponse;
  private PaginationPostDto paginationPostDto;

  public PostsFeature(
      @Value("${api.base.url}") final String apiBaseUri,
      @Value("${api.posts.context.path}") final String apiBaseContextPath,
      @Value("${api.posts.add}") final String addUrl,
      @Value("${api.posts.user}") final String userUrl,
      final ObjectMapper objectMapper,
      final AuthStateService authStateService,
      final ResponseCodeState responseCodeState) {
    super(
        apiBaseUri,
        apiBaseContextPath,
        objectMapper,
        ContentType.JSON,
        authStateService,
        responseCodeState);
    this.addUrl = addUrl;
    this.userUrl = userUrl;
  }

  @When("Make a request for a post {string}")
  public void makeARequestForAPost(final String id) {
    final ExtractableResponse<Response> postResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .get(id)
            .then()
            .extract();
    responseCodeState.setResponseCode(postResponse.statusCode());
    this.postDto = postResponse.as(PostDto.class);
  }

  @Then("Response should has {string} and {string}")
  public void responseShouldHasAnd(final String id, final String title) {
    assertEquals(this.postDto.getId(), Integer.parseInt(id));
    assertEquals(this.postDto.getTitle(), title);
  }

  @Given("Create a post metadata with")
  public void createAPostMetadataWith(final PostDto postDto) {
    this.postDto = postDto;
  }

  @When("Make a request to create a post")
  public void makeARequestToCreateAPost() {
    final ExtractableResponse<Response> addPostResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .contentType(ContentType.JSON)
            .body(this.postDto)
            .when()
            .post(this.addUrl)
            .then()
            .extract();
    responseCodeState.setResponseCode(addPostResponse.statusCode());
    this.postDtoResponse = addPostResponse.as(PostDto.class);
  }

  @Then("Response metadata should match created post")
  public void responseMetadataShouldMatchCreatedPost() {
    assertEquals(this.postDto.getTitle(), this.postDtoResponse.getTitle());
    assertEquals(this.postDto.getReactions(), this.postDtoResponse.getReactions());
    assertEquals(this.postDto.getUserId(), this.postDtoResponse.getUserId());
  }

  @When("Make a request to update a post {string}")
  public void makeARequestToUpdateAPost(final String id) {
    final ExtractableResponse<Response> updatePostResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .contentType(ContentType.JSON)
            .body(this.postDto)
            .when()
            .put(id)
            .then()
            .extract();
    responseCodeState.setResponseCode(updatePostResponse.statusCode());
    this.postDtoResponse = updatePostResponse.as(PostDto.class);
  }

  @Then("Response metadata should match updated post")
  public void responseMetadataShouldMatchUpdatedPost() {
    assertEquals(this.postDto.getTitle(), this.postDtoResponse.getTitle());
    assertEquals(this.postDto.getReactions(), this.postDtoResponse.getReactions());
    assertEquals(this.postDto.getUserId(), this.postDtoResponse.getUserId());
    //    assertEquals(this.postDto.getViews(), this.postDtoResponse.getViews()); // API does not
    // update views
  }

  @When("Make a request to delete a post {string}")
  public void makeARequestToDeleteAPost(final String id) {
    final ExtractableResponse<Response> deletePostResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .delete(id)
            .then()
            .extract();
    responseCodeState.setResponseCode(deletePostResponse.statusCode());
    this.postDtoResponse = deletePostResponse.as(PostDto.class);
  }

  @When("Make a request for user posts")
  public void makeARequestForUserPosts() {
    ExtractableResponse<Response> userPostsResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .get(userUrl + "/" + authStateService.getAuthUserId())
            .then()
            .extract();
    responseCodeState.setResponseCode(userPostsResponse.statusCode());
    this.paginationPostDto = userPostsResponse.as(PaginationPostDto.class);
  }

  @When("Make a request for posts with pagination {string}")
  public void makeARequestForPostsWithPagination(final String limit) {
    final ExtractableResponse<Response> paginationPostsResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .queryParam("limit", limit)
            .when()
            .get()
            .then()
            .extract();
    responseCodeState.setResponseCode(paginationPostsResponse.statusCode());
    this.paginationPostDto = paginationPostsResponse.as(PaginationPostDto.class);
  }

  @Then("Response should has total posts {string}")
  public void responseShouldHasTotalPosts(final String totalPosts) {
    assertEquals(this.paginationPostDto.getLimit(), Integer.parseInt(totalPosts));
    assertEquals(this.paginationPostDto.getPosts().size(), Integer.parseInt(totalPosts));
  }

  @Then("Response post should be deleted")
  public void responsePostShouldBeDeleted() {
    assertTrue(this.postDtoResponse.getIsDeleted());
  }

  @DocStringType(contentType = "json")
  public PostDto postDto(final String postJson) throws JsonProcessingException {
    final PostDto postDto1 = objectMapper.readValue(postJson, PostDto.class);
    postDto1.setUserId(authStateService.getAuthUserId());

    return postDto1;
  }
}
