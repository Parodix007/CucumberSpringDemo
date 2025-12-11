package com.cucumber_spring.demo.cucumber_spring_demo.features.posts.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginationPostDto {
  private List<PostDto> posts;
  private int total;
  private int skip;
  private int limit;
}
