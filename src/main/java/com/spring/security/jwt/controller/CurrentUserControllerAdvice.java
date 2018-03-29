package com.spring.security.jwt.controller;

import com.spring.security.jwt.domain.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice(basePackages = "com.spring.security.jwt.controller")
@Order(1)
public class CurrentUserControllerAdvice {

    @Autowired
    public CurrentUserControllerAdvice() {
    }

    @ModelAttribute("username")
    public String getCurrentUser(Authentication authentication) {
        return (authentication == null) ? null : ((JwtUser) authentication.getPrincipal()).getUsername();
    }

}
