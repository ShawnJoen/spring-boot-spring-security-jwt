package com.spring.security.jwt.service.impl;

import com.spring.security.jwt.domain.CurrentUser;
import com.spring.security.jwt.domain.User;
import com.spring.security.jwt.repository.UserDao;
import com.spring.security.jwt.service.UserService;
import com.spring.security.jwt.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    final private AuthenticationManager authenticationManager;
    final private UserDetailsService userDetailsService;
    final private JwtTokenUtil jwtTokenUtil;
    final private UserDao userDao;

    @Autowired
    public UserServiceImpl(final AuthenticationManager authenticationManager, final UserDetailsService userDetailsService,
                           final JwtTokenUtil jwtTokenUtil, final UserDao userDao) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDao = userDao;
    }

    @Override
    public CurrentUser login(final String email, final String pw) {
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(email, pw);

            Authentication authentication = authenticationManager.authenticate(upToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.info("-----------用户验证失败 email {}", email);
            return null;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            log.info("-----------用户不存在 email {}", email);
            return null;
        }

        final User user = userDao.findByEmail(email);

        final CurrentUser currentUser = new CurrentUser();
        currentUser.setId(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setToken(jwtTokenUtil.generateToken(userDetails));

        return currentUser;
    }

    @Override
    public String register(final User user) {
        final String username = user.getEmail();
        if (userDao.findByEmail(username) != null) {
            return "用户已存在";
        }
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = user.getPw();
        user.setPw(encoder.encode(rawPassword));
        user.setRole("ROLE_USER");//ROLE_ADMIN
        user.setRegDate(LocalDateTime.now());
        userDao.saveUser(user);
        return "success";
    }

    @Override
    public String refreshToken(final String oldToken) {
        final String token = oldToken.substring("Shawn ".length());
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return "error";
    }
}
