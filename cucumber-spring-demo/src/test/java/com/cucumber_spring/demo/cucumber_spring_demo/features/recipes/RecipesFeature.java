package com.cucumber_spring.demo.cucumber_spring_demo.features.recipes;

import com.cucumber_spring.demo.cucumber_spring_demo.auth.AuthService;
import com.cucumber_spring.demo.cucumber_spring_demo.auth.model.AuthUserDto;
import com.cucumber_spring.demo.cucumber_spring_demo.features.Feature;
import com.cucumber_spring.demo.cucumber_spring_demo.features.recipes.model.RecipeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RecipesFeature extends Feature {
  private final AuthService authService;
  private final String addUrl;
  private RecipeDto recipeDto;
  private int statusCode;

  RecipesFeature(
      @Value("${api.base.url}") final String apiBaseUrl,
      @Value("${api.recipes.context.path}") final String apiRecipesContextPath,
      @Value("${api.recipes.add}") final String addUrl,
      final ObjectMapper objectMapper,
      final AuthService authService) {
    super(apiBaseUrl, apiRecipesContextPath, objectMapper, ContentType.JSON);
    this.authService = authService;
    this.addUrl = addUrl;
  }

  @DataTableType
  public AuthUserDto authUserDto(final Map<String, String> userMetadata) {
    return objectMapper.convertValue(userMetadata, AuthUserDto.class);
  }

  @Given("Authorize a user with")
  public void authorizeAUserWith(final List<AuthUserDto> authUserDtos) {
    authService.createDataForAuth(authUserDtos.getFirst());
  }

  @Given("Create a new recipe with")
  public void createANewRecipeWith(final Map<String, String> recipeMetadata) {
    this.recipeDto = objectMapper.convertValue(recipeMetadata, RecipeDto.class);
    log.info("A recipe dto {}", this.recipeDto);
  }

  @When("Make a request with the new recipe")
  public void makeARequestWithTheNewRecipe() {
    final ExtractableResponse<Response> addRecipeResponse =
        RestAssured.given()
            .spec(this.requestSpecBuilder.build())
            .body(this.recipeDto)
            .contentType(ContentType.JSON)
            .when()
            .post(addUrl)
            .then()
            .extract();

    log.info("An add recipe response {}", addRecipeResponse);

    this.statusCode = addRecipeResponse.statusCode();
  }

  @Given("Get a recipe id {string}")
  public void getARecipeId(String id) {}

  @When("Make a request for a recipe")
  public void makeARequestForARecipe() {}

  @Then("The recipe name should match {string}")
  public void theRecipeNameShouldMatch(String expectedName) {}

  @Given("Create a new recipe model with")
  public void createANewRecipeModelWith() {}

  @When("Make a update request withe the recipe model for {string}")
  public void makeAUpdateRequestWitheTheRecipeModelFor(String id) {}

  @When("Make a delete request for recipe {int}")
  public void makeADeleteRequestForRecipe(int id) {}
}
