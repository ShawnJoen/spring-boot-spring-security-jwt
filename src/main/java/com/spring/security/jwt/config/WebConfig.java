package com.spring.security.jwt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.security.jwt.filter.JwtAuthenticationTokenFilter;
import com.spring.security.jwt.util.LocalDateTimeConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

//    @Bean
//    public JwtAuthenticationTokenFilter getLocaleInterceptor() {
//        return new JwtAuthenticationTokenFilter();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(JwtAuthenticationTokenFilter());
//    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final GsonHttpMessageConverter msgConverter = new GsonHttpMessageConverter();
        msgConverter.setGson(gson());

        converters.add(msgConverter);
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        converters.add(new FormHttpMessageConverter());
    }

    @Bean
    public Gson gson() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        return gson;
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
