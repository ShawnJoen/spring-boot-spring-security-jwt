package com.spring.security.jwt.filter;

import com.spring.security.jwt.service.impl.JwtUserDetailsServiceImpl;
import com.spring.security.jwt.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter() {
        this.userDetailsService = getUserDetailsService();
        this.jwtTokenUtil = getJwtTokenUtil();
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new JwtUserDetailsServiceImpl();
    }

    @Bean
    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String tokenHead = "Shawn ";
        if (authHeader != null && authHeader.startsWith(tokenHead)) {

            final String authToken = authHeader.substring(tokenHead.length());
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
