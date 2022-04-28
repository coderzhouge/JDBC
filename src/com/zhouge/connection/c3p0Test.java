package com.zhouge.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;


public class c3p0Test {

    public static void main(String[] args) throws SQLException {
        System.out.println(new c3p0Test().getConnection());
    }

    /**
     * 获取连接方式一:使用传统方法
     * @throws Exception
     */
    @Test
    public void testGetConnection() throws Exception {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/db02?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true");
        cpds.setUser("root");
        cpds.setPassword("root");

        //设置相关参数
        cpds.setInitialPoolSize(10);
        cpds.setMaxPoolSize(100);
        cpds.setMinPoolSize(10);

        //获取连接
        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁c3p0数据库连接池 一般不建议销毁
        DataSources.destroy(cpds);
    }

    /**
     * 获取连接方式二:使用配置文件
     */
    @Test
    public void testGetConnection1() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource("test");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

    /**
     * 保证池子只有一个
     */
    static ComboPooledDataSource cpds = new ComboPooledDataSource("test");

    /**
     * 使用c3p0获取连接
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {

        Connection conn = cpds.getConnection();
        return conn;
    }
}

