package com.example.my_blog.dto;

import lombok.Getter;

@Getter
public class CreateAccessTokenRequest {
    private String refreshToken;
}
