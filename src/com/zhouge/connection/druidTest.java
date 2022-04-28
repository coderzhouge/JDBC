package com.zhouge.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class druidTest {

    private static DataSource source ;

    static{

        try {
            Properties properties = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(is);
            source = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用druid数据库连接池技术获取连接
     */
    @Test
    public void testGetConnection(){

        try {

            Connection conn = source.getConnection();
            System.out.println(conn);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
