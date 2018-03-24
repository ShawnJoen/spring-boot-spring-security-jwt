package com.spring.security.jwt.filter;

import com.spring.security.jwt.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.spring.security.jwt.config.WebSecurityConfig.AUTHENTICATION_HEADER_NAME;
import static com.spring.security.jwt.config.WebSecurityConfig.HEADER_PREFIX;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter() {}

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHENTICATION_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(HEADER_PREFIX)) {

            final String authToken = authHeader.substring(HEADER_PREFIX.length());
            final String username = this.jwtTokenUtil.getUsernameFromToken(authToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                log.info("-----------JwtAuthenticationTokenFilter doFilterInternal 3 {}", username);

                final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (this.jwtTokenUtil.validateToken(authToken, userDetails)) {

                    log.info("-----------JwtAuthenticationTokenFilter doFilterInternal 4");

                    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
