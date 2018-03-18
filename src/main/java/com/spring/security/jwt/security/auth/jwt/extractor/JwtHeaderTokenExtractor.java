package com.spring.security.jwt.security.auth.jwt.extractor;

import com.spring.security.jwt.config.WebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 */
@Slf4j
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    @Override
    public String extract(String header) {

        log.info("------------ JwtHeaderTokenExtractor extract {}", header);
        log.info("------------ JwtHeaderTokenExtractor extract {}", WebSecurityConfig.HEADER_PREFIX);

        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < WebSecurityConfig.HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return header.substring(WebSecurityConfig.HEADER_PREFIX.length(), header.length());
    }
}
