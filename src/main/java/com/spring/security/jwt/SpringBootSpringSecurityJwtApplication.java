package com.spring.security.jwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启事物环境 然后 Service方法 添加注解 @Transactional
@MapperScan("com.spring.security.jwt.repository")//指定dao接口包进行扫描
public class SpringBootSpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSpringSecurityJwtApplication.class, args);
	}
}
