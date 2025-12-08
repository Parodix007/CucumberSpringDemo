package com.cucumber_spring.demo.cucumber_spring_demo.features.recipes;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthStateService;
import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserDto;
import com.cucumber_spring.demo.cucumber_spring_demo.config.model.ResponseCodeState;
import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.features.recipes.model.RecipeDeleteResVo;
import com.cucumber_spring.demo.cucumber_spring_demo.features.recipes.model.RecipeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DocStringType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RecipesFeature extends Feature {
  private final AuthStateService authStateService;
  private final String addUrl;
  private final ResponseCodeState responseCodeState;
  private RecipeDto recipeDto;
  private String recipeToGetId;
  private RecipeDeleteResVo recipeDeleteResVo;

  private RecipesFeature(
      @Value("${api.base.url}") final String apiBaseUrl,
      @Value("${api.recipes.context.path}") final String apiRecipesContextPath,
      @Value("${api.recipes.add}") final String addUrl,
      final ObjectMapper objectMapper,
      final AuthStateService authStateService,
      final ResponseCodeState responseCodeState) {
    super(apiBaseUrl, apiRecipesContextPath, objectMapper, ContentType.JSON);
    this.authStateService = authStateService;
    this.addUrl = addUrl;
    this.responseCodeState = responseCodeState;
  }

  @DataTableType
  public AuthUserDto authUserDto(final Map<String, String> userMetadata) {
    return objectMapper.convertValue(userMetadata, AuthUserDto.class);
  }

  @DocStringType(contentType = "json")
  public RecipeDto recipeDto(final String recipeMetadata) throws JsonProcessingException {
    return objectMapper.readValue(recipeMetadata, RecipeDto.class);
  }

  @Given("Authorize a user with")
  public void authorizeAUserWith(final AuthUserDto authUserDto) {
    authStateService.createDataForAuth(authUserDto);
    authStateService.makeARequestForJwt();
  }

  @Given("Create a new recipe with")
  public void createANewRecipeWith(final RecipeDto recipeDto) {
    this.recipeDto = recipeDto;
    log.info("A recipe dto {}", this.recipeDto);
  }

  @When("Make a request with the new recipe")
  public void makeARequestWithTheNewRecipe() {
    final ExtractableResponse<Response> addRecipeResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .body(this.recipeDto)
            .contentType(ContentType.JSON)
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .post(addUrl)
            .then()
            .extract();

    log.info("An add recipe response {}", addRecipeResponse);

    responseCodeState.setResponseCode(addRecipeResponse.statusCode());
  }

  @Given("Get a recipe id {string}")
  public void getARecipeId(final String id) {
    this.recipeToGetId = id;
  }

  @When("Make a request for a recipe")
  public void makeARequestForARecipe() {
    final ExtractableResponse<Response> getRecipeResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .get(this.recipeToGetId)
            .then()
            .extract();

    responseCodeState.setResponseCode(getRecipeResponse.statusCode());
    this.recipeDto = getRecipeResponse.as(RecipeDto.class);
  }

  @Then("The recipe name should match {string}")
  public void theRecipeNameShouldMatch(final String expectedName) {
    assertEquals(expectedName, this.recipeDto.getName());
  }

  @Given("Create a new recipe model with")
  public void createANewRecipeModelWith(final RecipeDto recipeDto) {
    this.recipeDto = recipeDto;
  }

  @When("Make a update request withe the recipe model for {string}")
  public void makeAUpdateRequestWitheTheRecipeModelFor(final String id) {
    final ExtractableResponse<Response> updateRecipeResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .body(this.recipeDto)
            .contentType(ContentType.JSON)
            .when()
            .put(id)
            .then()
            .extract();
    responseCodeState.setResponseCode(updateRecipeResponse.statusCode());
    this.recipeDto = updateRecipeResponse.as(RecipeDto.class);
  }

  @Then("Response should has new name {string}")
  public void responseShouldHasNewName(final String expectedNewName) {
    assertEquals(expectedNewName, this.recipeDto.getName());
  }

  @When("Make a delete request for recipe {int}")
  public void makeADeleteRequestForRecipe(final int id) {
    final ExtractableResponse<Response> deleteRecipeResponse = RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .auth()
            .oauth2(authStateService.getUserJwt())
            .when()
            .delete(String.valueOf(id))
            .then()
            .extract();
    final int deleteRecipeResponseStatusCode = deleteRecipeResponse.statusCode();

    responseCodeState.setResponseCode(deleteRecipeResponseStatusCode);

    this.recipeDeleteResVo = deleteRecipeResponse.as(RecipeDeleteResVo.class);

    log.info("Delete for a recipe response {}", this.recipeDeleteResVo);
  }

    @And("Response isDeleted should be true for id {int}")
    public void responseIsDeletedShouldBeTrue(final int id) {
      assertTrue(this.recipeDeleteResVo.isDeleted());
      assertEquals(id, this.recipeDeleteResVo.getId());
    }
}
