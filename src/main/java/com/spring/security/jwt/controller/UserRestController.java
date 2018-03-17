package com.spring.security.jwt.controller;

import com.spring.security.jwt.domain.CurrentUser;
import com.spring.security.jwt.domain.Response;
import com.spring.security.jwt.domain.User;
import com.spring.security.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

//http://localhost:8080/user/doLogin?email=aaaa&pw=aaaa
    /**
     * 用户登录
     */
    @PostMapping(value = "/doLogin", params = {"email", "pw"})
    public Response<CurrentUser> doLogin(String email, String pw) {
        CurrentUser user = userService.login(email, pw);
        if (user == null) {
            return Response.<CurrentUser>builder()
                    .status(0)
                    .message("failed")
                    .data(null)
                    .build();
        } else {
            return Response.<CurrentUser>builder()
                    .status(0)
                    .message("success")
                    .data(user)
                    .build();
        }
    }
//http://localhost:8080/user/doRegister?email=aaaa&pw=aaaa
    /**
     * 用户注册
     */
    @GetMapping(value = "/doRegister")
    public Response<String> doRegister(User user) {
        return Response.<String>builder()
                .status(0)
                .message(userService.register(user))
                //.data()
                .build();
    }
    /*
    * 1.登入获取Token（过期时间为15分钟）
    * 2.使用Post Man 访问 http://localhost:8080/refreshToken Headers: (Authorization(key), Shawn Token)
    * 如验证失败返回 Json:{
    *      "timestamp": 1521298939828,
    *      "status": 403,
    *      "error": "Forbidden",
    *      "message": "Access Denied",
    *      "path": "/dashboard"
    *   }
    * */
    /**
     * 刷新密钥
     * @param authorization 原密钥
     * @return 新密钥
     */
    @GetMapping(value = "/refreshToken")
    public String refreshToken(@RequestHeader String authorization) {
        return userService.refreshToken(authorization);
    }

}
