package com.zhouge.jdbc;

public class OracleJdbcImpl implements JdbcInterface {

    String oracle = "Oracle";

    @Override
    public Object getConnection() {
        System.out.println("获取 "+oracle+" 连接");
        return null;
    }

    @Override
    public void curd() {
        System.out.println("完成 "+oracle+" 增删查改");
    }

    @Override
    public void close() {
        System.out.println("关闭 "+oracle+" 连接");
    }
}
