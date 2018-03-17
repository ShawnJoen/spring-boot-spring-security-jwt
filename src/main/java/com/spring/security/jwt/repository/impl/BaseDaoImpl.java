package com.spring.security.jwt.repository.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDaoImpl {

	@Autowired
	private SqlSession sqlSession;
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}
	
    public String getStatement(final String sqlId) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append(".").append(sqlId);
        return sb.toString();
    }
}