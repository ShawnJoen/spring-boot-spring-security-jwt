package com.spring.security.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/view")
public class UserController {

    @GetMapping(value = "/login")
    public String login() {

        log.info("--------- login");

        return "login";
    }

    @GetMapping(value = "/register")
    public String register() {

        log.info("--------- register");

        return "register";
    }

}
