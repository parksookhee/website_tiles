package com.site.commons.lib.mybatis;

public class UserSqlSessionFactory {
    private UserSqlSessionMaster sqlMaster;
    private UserSqlSessionSlave sqlSlave;
    private UserSqlSessionMasterSelect sqlMasterSelect;
   
    public UserSqlSessionFactory(){}
   
    /**
     * @breif  생성
     * @param sqlMaster Master 처리용 메서드를 포함한 UserSqlSessionMaster 객체
     * @param sqlSlave Master 처리용 메서드를 포함한 UserSqlSessionSlave 객체
     * @param sqlMasterSelect Master select 처리용 메서드를 포함한 UserSqlSessionMasterSelect 객체
     */
    public UserSqlSessionFactory(UserSqlSessionMaster sqlMaster, UserSqlSessionSlave sqlSlave, UserSqlSessionMasterSelect sqlMasterSelect){
        this.sqlMaster = sqlMaster;
        this.sqlSlave = sqlSlave;
        this.sqlMasterSelect = sqlMasterSelect;
    }

    /**
     * @breif  조회 DB
     * @return  UserSqlSessionSlave 객체
     */
    public UserSqlSessionSlave select() throws Exception {
        return sqlSlave;
    }
   
    /**
     * @breif  입력/수정/삭제 DB
     * @return  UserSqlSessionMaster 객체
     */
    public UserSqlSessionMaster cud() throws Exception {
        return sqlMaster;
    }
   
    /**
     * @breif  Master DB 조
     * @return  UserSqlSessionMasterSelect 객체
     */
    public UserSqlSessionMasterSelect masterSelect() throws Exception{
        return sqlMasterSelect;
    }
}
