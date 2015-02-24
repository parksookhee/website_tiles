package com.site.commons.lib.mybatis;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class UserSqlSessionSlave {
    private SqlSession sqlSession;
   
    /**
     * @breif 생성자
     * @param sqlSession Master연결에 필요한 SqlSession 객체
     */
    public UserSqlSessionSlave(SqlSession sqlSession){
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

    public <E> List<E> selectList(String statement, Object parameter,
            RowBounds rowBounds) throws Exception {
        return sqlSession.selectList(statement, parameter, rowBounds);
    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey) throws Exception {
        return sqlSession.selectMap(statement, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
            String mapKey) throws Exception {
        return sqlSession.selectMap(statement, parameter, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter,
            String mapKey, RowBounds rowBounds) {
        return sqlSession.selectMap(statement, parameter, mapKey, rowBounds);
    }
}
