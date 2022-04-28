package com.zhouge.jdbc;

public interface JdbcInterface {
    //连接
    public Object getConnection();

    //crud
    public void curd();

    //关闭连接
    public void close();

}
