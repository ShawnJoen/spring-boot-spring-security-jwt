package com.spring.security.jwt.util;

import com.spring.security.jwt.domain.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    /**
     * 密钥
     */
    private final String secretKey = "ShawnsSecret";

    /**
     * 从数据声明生成令牌
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(final Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + 60 * 1000 * 15);//过期时间 毫秒单位
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("从令牌中获取数据 {}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成令牌
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(final String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getSubject();
            }
        } catch (Exception e) {
            log.debug("从令牌中获取用户名 {}", e.getMessage());
        }
        return null;
    }

    /**
     * 判断令牌是否过期
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(final String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.debug("判断令牌是否过期 {}", e.getMessage());
            return false;
        }
    }

    /**
     * 刷新令牌
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(final String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            return generateToken(claims);
        } catch (Exception e) {
            log.debug("刷新令牌 {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
