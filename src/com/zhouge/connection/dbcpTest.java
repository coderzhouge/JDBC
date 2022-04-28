package com.zhouge.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class dbcpTest {

    /**
     * 方式一:使用硬编码方式获取连接
     */
    @Test
    public void testGetConnection(){

        try {
            //创建dbcp数据库连接池
            BasicDataSource source = new BasicDataSource();

            //设置参数
            source.setDriverClassName("com.mysql.cj.jdbc.Driver");
            source.setUrl("jdbc:mysql://127.0.0.1:3306/db02?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true");
            source.setUsername("root");
            source.setPassword("root");

            source.setInitialSize(10);
            source.setMaxActive(20);

            //获取连接
            Connection conn = source.getConnection();

            //打印信息
            System.out.println(conn);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * 方式二:使用配置文件获取连接
     */
    @Test
    public void testGetConnection01(){

        try {
            //创建配置文件
            Properties properties = new Properties();

            //读取配置文件信息 任选其一
                //方式一:使用系统读取流
                //ClassLoader.getSystemClassLoader().getResourceAsStream("");

                //方式二:使用文件输入流
                FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(fis);//加载文件信息

            //创建dbcp数据库连接池
            DataSource ds = BasicDataSourceFactory.createDataSource(properties);

            //获取连接
            Connection conn = ds.getConnection();

            //打印信息
            System.out.println(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*------------------------------------------------*/

    /**
     * 改进方式二
     */
    private static DataSource ds ;

    static{
        try {
            //创建配置文件
            Properties properties = new Properties();

            //加载文件信息
            FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(fis);

            //创建dbcp数据库连接池
            ds = BasicDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetConnection02(){


        try {
            //获取连接
            Connection conn = ds.getConnection();

            //打印信息
            System.out.println(conn);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
