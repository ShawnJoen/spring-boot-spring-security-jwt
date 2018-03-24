package com.spring.security.jwt.service.impl;

import com.spring.security.jwt.domain.JwtUser;
import com.spring.security.jwt.domain.User;
import com.spring.security.jwt.repository.UserDao;
import com.spring.security.jwt.security.config.JwtSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Slf4j
//@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UserDao userDao;
    @Autowired private RedisTemplate redisTemplate;
    @Autowired private JwtSettings jwtSettings;

    public JwtUserDetailsServiceImpl() {}
    /*
    * 在Service
    *   @Autowired
    *   UserDetailsService userDetailsService;
    * 判断用户存在与否 如 存在返回 实现了接口UserDetails的对象
    *   UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    * 生成Jwt Token
    *   return jwtTokenUtil.generateToken(userDetails)
    * */
    @Override
    public UserDetails loadUserByUsername(String username) {

        User user;

        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        final String key = DigestUtils.md5Hex(username);
        final boolean hasKey = redisTemplate.hasKey(key);

        log.info("----------UserDetailsService User key: {}", key);

        if (hasKey) {
            //从缓存中获取用户信息
            user = operations.get(key);
            if (user != null && user.getEmail().equals(username)) {

                log.info("----------UserDetailsService UserDetails from cache: {}", user);

                return new JwtUser(user);
            }
        }

        user = userDao.findByEmail(username);
        if (user == null) {
            return null;
        } else {
            /*
            * 存用户的权限信息
            * List<GrantedAuthority> list = new ArrayList<GrantedAuthority>()
            * GrantedAuthority au = new SimpleGrantedAuthority("ROLE_USER")
            * list.add(au)
            * List<? extends GrantedAuthority> authorities = list
            * */
            log.info("----------UserDetailsService UserDetails from database: {}", user);
            //保存到缓存中
            operations.set(key, user, jwtSettings.getTokenExpirationTime(), TimeUnit.MINUTES);//MINUTES分, MILLISECONDS毫秒单位, SECONDS秒

            return new JwtUser(user);
        }
    }
}
