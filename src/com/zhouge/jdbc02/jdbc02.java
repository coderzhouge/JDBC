package com.zhouge.jdbc02;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.util.Properties;

public class jdbc02 {
    public static void main(String[] args) throws Exception {
        ReflexConn();
    }

    /**
     * 方式二:使用反射方式获取数据库连接.
     * 避免程序中出现第三方api
     */
    public static void ReflexConn() throws Exception {
        //使用反射,找到Driver
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/db01?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        Properties info  = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","root");
        Connection connection = driver.connect(url, info);

        //执行sql
        String sql = "insert into t_actor values(null,'杨幂','女','1985-06-15','13488889999');";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sql);

        System.out.println(rows >= 1 ? "执行成功":"执行失败");

        //关闭连接
        statement.close();
        connection.close();


    }
}
