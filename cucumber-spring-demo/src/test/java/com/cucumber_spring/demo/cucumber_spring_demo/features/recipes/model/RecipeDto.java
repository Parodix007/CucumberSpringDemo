package com.cucumber_spring.demo.cucumber_spring_demo.features.recipes.model;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {
    private String name;
    private List<String> tags;
    private List<String> mealType;
    private int reviewCount;
}
