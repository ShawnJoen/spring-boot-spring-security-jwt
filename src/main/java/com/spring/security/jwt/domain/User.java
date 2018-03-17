package com.spring.security.jwt.domain;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

    private Long id;

    private String email;

    private String pw;

    private String role;

    private LocalDateTime regDate;
}
