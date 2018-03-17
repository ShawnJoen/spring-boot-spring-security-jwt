package com.spring.security.jwt.repository.impl;

import com.spring.security.jwt.domain.User;
import com.spring.security.jwt.repository.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Override
    public User findByEmail(String email) {
        return getSqlSession().selectOne(getStatement("findByEmail"), email);
    }

    @Override
    public int saveUser(User user) {
        return getSqlSession().insert(getStatement("saveUser"), user);
    }
}
