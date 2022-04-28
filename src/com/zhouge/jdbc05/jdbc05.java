package com.zhouge.jdbc05;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * final版
 */
public class jdbc05 {
    public static void main(String[] args) throws Exception {
        finalConn();

    }

    public static void finalConn() throws Exception {

        //获取配置文件信息
        InputStream resource = jdbc05.class.getClassLoader().getResourceAsStream("jdbc.properties");

        //读取配置文件信息
        Properties properties = new Properties();
        properties.load(resource);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //加载驱动
       Class.forName(driverClass);

       //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        //执行sql
        String sql = "insert into t_actor values(null,'易烊千玺','男','2000-12-05','18644446666');";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sql);

        System.out.println(rows >= 1? "执行成功":"执行失败");

        //释放资源
        statement.close();
        connection.close();


    }

}
