package com.cucumber_spring.demo.cucumber_spring_demo.features.posts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
  private Integer id;
  private String title;
  private String body;
  private List<String> tags;
  private PostReactionsDto reactions;
  private Integer views;
  private Integer userId;
  private Boolean isDeleted;

  @Data
  @NoArgsConstructor
  public static final class PostReactionsDto {
    private Integer likes;
    private Integer dislikes;
  }
}
