package com.spring.security.jwt.config;

import com.spring.security.jwt.security.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    * 必须@Autowired AuthenticationManagerBuilder配置
    * UserDetailsService 加密方式 不然不能通过 .authenticate(new UsernamePasswordAuthenticationToken(账号, 密码))
    * */
    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());//使用BCrypt进行密码的hash
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        //不做验证的Url
        final List<String> permitAllEndpointList = Arrays.asList("/static/**", "/view/**", "/user/**");
        //验证的Url
        //List<String> validateEndpointList = Arrays.asList("/api/**", "/**");
        final String validateEndpointList = "/**";

        httpSecurity
                .csrf().disable()//关闭csrf验证
                //加权限不足,身份验证失败的 返回值处理
                .exceptionHandling()
                    .authenticationEntryPoint(this.authenticationEntryPoint)//身份验证失败
                .and()
                    //基于token，使用无状态的Session机制(即Spring不使用HTTPSession)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                    .permitAll()
                    //所有请求需要身份验证
                    //.anyRequest().authenticated()
                .and()
                    .authorizeRequests()
                    //.antMatchers(validateEndpointList.toArray(new String[validateEndpointList.size()]))
                    .antMatchers(validateEndpointList)
                    .authenticated()
                .and()
//                    .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(
//                                permitAllEndpointList,
//                                validateEndpointList
//                        ), UsernamePasswordAuthenticationFilter.class);
                    //禁用缓存
                    .headers().cacheControl();
    }

//    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
//    @Autowired private TokenExtractor tokenExtractor;
//    @Autowired private AuthenticationManager authenticationManager;
//
//    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern)
//            throws Exception {
//        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
//        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(tokenExtractor, matcher);//failureHandler, tokenExtractor, matcher
//        filter.setAuthenticationManager(this.authenticationManager);
//        return filter;
//    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(jwtAuthenticationProvider);
//    }

//    private SkipPathRequestMatcher skipPathRequestMatcher() {
//        return new SkipPathRequestMatcher(Arrays.asList(LOGIN_END_POINT, TOKEN_END_POINT));
//    }
//
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipPathRequestMatcher());
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setAuthenticationFailureHandler(securityHandler);
//        return filter;
//    }
}
