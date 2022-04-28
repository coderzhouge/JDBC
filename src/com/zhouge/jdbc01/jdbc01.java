package com.zhouge.jdbc01;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class jdbc01 {

    public static void main(String[] args) throws SQLException {
        DriverConn();
    }

    /**
     * 方式一:使用Driver连接数据库
     * @throws SQLException
     */
    public static void DriverConn() throws SQLException {

        //注册驱动
        java.sql.Driver driver = new Driver();
        String url = "jdbc:mysql://127.0.0.1:3306/db01?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";

        //将用户名和密码封装在properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","root");

        //建立连接
        Connection conn = driver.connect(url, info);

        //执行sql
        String sql = "insert into t_actor values(null,'李易峰','男','1987-12-15','13455556666');";
        String sql1 = "update t_actor set name = '李易峰' where id = 3 ";
        Statement statement = conn.createStatement();
        int rows = statement.executeUpdate(sql1);

        System.out.println(rows >= 1 ?"执行成功":"执行失败");

        //释放资源
        statement.close();
        conn.close();

    }



}


