package com.cucumber_spring.demo.cucumber_spring_demo.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthUserResDto {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
    private String accessToken;
    private String refreshToken;
}
