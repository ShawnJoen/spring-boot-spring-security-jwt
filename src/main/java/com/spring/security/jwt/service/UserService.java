package com.spring.security.jwt.service;

import com.spring.security.jwt.domain.CurrentUser;
import com.spring.security.jwt.domain.User;

public interface UserService {
    /**
     * 用户登录
     */
    CurrentUser login(String email, String pw);

    /**
     * 用户注册
     */
    String register(User user);

    /**
     * 刷新密钥
     */
    String refreshToken(String oldToken);
}
