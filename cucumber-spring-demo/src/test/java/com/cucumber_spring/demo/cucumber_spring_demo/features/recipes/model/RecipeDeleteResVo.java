package com.cucumber_spring.demo.cucumber_spring_demo.features.recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeDeleteResVo {
    private int id;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
