package com.spring.security.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class InfoController {

    /*
    * 1.登入获取Token（过期时间为15分钟）
    * 2.使用Post Man 访问 http://localhost:8080/dashboard Headers: (Authorization(key), Shawn Token)
    * 如验证失败返回 Json:{
    *      "timestamp": 1521298939828,
    *      "status": 403,
    *      "error": "Forbidden",
    *      "message": "Access Denied",
    *      "path": "/dashboard"
    *   }
    * */
    @GetMapping(value = "/dashboard")
    public String dashboard() {

        log.info("--------- dashboard");

        return "dashboard";
    }
}
