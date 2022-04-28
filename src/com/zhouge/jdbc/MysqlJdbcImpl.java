package com.zhouge.jdbc;

public class MysqlJdbcImpl implements JdbcInterface {

    String mysql = "mysql";

    @Override
    public Object getConnection() {
        System.out.println("获取 "+mysql+" 连接");
        return null;
    }

    @Override
    public void curd() {
        System.out.println("完成 "+mysql+" 增删查改");
    }

    @Override
    public void close() {
        System.out.println("关闭 "+mysql+" 连接");
    }
}
