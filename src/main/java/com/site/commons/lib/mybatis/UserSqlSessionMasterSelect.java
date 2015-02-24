package com.site.commons.lib.mybatis;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserSqlSessionMasterSelect {
    private SqlSession sqlSession;

    /**
     * @breif 생성자
     * @param sqlSession Master연결에 필요한 SqlSession 객체
     */
    public UserSqlSessionMasterSelect(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
   
    public <T> T selectOne(String statement) throws Exception {
        return sqlSession.selectOne(statement);
    }

    public <T> T selectOne(String statement, Object parameter) throws Exception {
        return sqlSession.selectOne(statement, parameter);
    }

    public <E> List<E> selectList(String statement) throws Exception {
        return sqlSession.selectList(statement);
    }

    public <E> List<E> selectList(String statement, Object parameter) throws Exception {
        return sqlSession.selectList(statement, parameter);
    }
}