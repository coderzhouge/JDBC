package com.zhouge.jdbc03;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

public class jdbc03 {
    public static void main(String[] args) throws Exception {
        DriverManagerConn();
    }

    /**
     * 使用DriverManager获取连接
     * @throws Exception
     */
    public static void DriverManagerConn() throws Exception {
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //使用DriverManager 注册驱动
        DriverManager.registerDriver(driver);

        //使用DriverManager获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/db01?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String user = "root";
        String password = "root";
        Connection connection = DriverManager.getConnection(url,user,password);

        //执行sql
        String sql = "insert into t_actor values(null,'杨紫','女','1985-07-06','13422226666');";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sql);

        System.out.println(rows >= 1? "执行成功":"执行失败");

        //关闭资源
        statement.close();
        connection.close();


    }
}
