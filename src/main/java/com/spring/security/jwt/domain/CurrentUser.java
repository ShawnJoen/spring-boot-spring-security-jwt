package com.spring.security.jwt.domain;

import lombok.Data;

@Data
public class CurrentUser {
    private Long id;
    private String email;
    private String token;
}
