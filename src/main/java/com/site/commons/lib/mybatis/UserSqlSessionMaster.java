package com.site.commons.lib.mybatis;

import org.apache.ibatis.session.SqlSession;

public class UserSqlSessionMaster{
    private SqlSession sqlSession;

    /**
     * @breif 생성자
     * @param sqlSession Master연결에 필요한 SqlSession 객체
     */
    public UserSqlSessionMaster(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
   
    public int insert(String statement) throws Exception {
        return sqlSession.insert(statement);
    }

    public int insert(String statement, Object parameter) throws Exception {
        return sqlSession.insert(statement, parameter);
    }

    public int update(String statement) throws Exception {
        return sqlSession.update(statement);
    }

    public int update(String statement, Object parameter) throws Exception {
        return sqlSession.update(statement, parameter);
    }

    public int delete(String statement) throws Exception {
        return sqlSession.delete(statement);
    }

    public int delete(String statement, Object parameter) throws Exception {
        return sqlSession.delete(statement, parameter);
    }
}
