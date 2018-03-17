package com.spring.security.jwt.repository;

import com.spring.security.jwt.domain.User;

public interface UserDao {
    User findByEmail(String email);
    int saveUser(User user);
}
