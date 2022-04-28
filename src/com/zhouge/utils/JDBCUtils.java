package com.zhouge.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtils {

    private static DataSource source = null ;
    public static Connection conn = null ;

    static {

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
     * 获取连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection(){

        Connection connection = null;

        try {
            //获取配置文件
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties info = new Properties();
            info.load(is);

            //读取配置文件信息
            String user = info.getProperty("user");
            String password = info.getProperty("password");
            String url2 = info.getProperty("url2");
            String driverClass = info.getProperty("driverClass");

            //加载驱动
            Class.forName(driverClass);

            //获取连接
            connection = DriverManager.getConnection(url2, user, password);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return  null;
        }finally {

            return connection;
        }

    }

    /**
     * 获取Druid数据库连接池的连接
     * @return
     */
    public static Connection getDruidPoolConnecion(){

        try {
            conn = source.getConnection();
            return conn;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return  null ;
    }

    /**
     * 释放资源，关闭连接
     * @param connection
     * @param st
     */
    public static void closeResource(Connection connection , Statement st ){

        try {
            if(st != null){
                st.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            if(connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * 释放资源,关闭连接
     * @param connection
     * @param st
     * @param rs
     */
    public static void closeResource(Connection connection , Statement st , ResultSet rs){

        try {
            if(st != null){
                st.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            if(connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            if(rs != null){
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * 释放DBUtils资源
     * @param connection
     * @param st
     * @param rs
     */
    public static void closeDBUtilsResource(Connection connection , Statement st ,ResultSet rs){
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(st);
        DbUtils.closeQuietly(rs);
    }

    /**
     * 通用的增删改查操作
     * @param sql
     * @param args
     */
    public static void update(String sql , Object ... args){
        Connection conn = null ;
        PreparedStatement pst = null ;


        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1 ,args[i]);
            }

            //执行sql
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                //释放资源
                JDBCUtils.closeResource(conn,pst);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 通用的增删改查操作
     * @param conn
     * @param sql
     * @param args
     */
    public static void update(Connection conn ,String sql , Object ... args){

        PreparedStatement pst = null ;


        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1 ,args[i]);
            }

            //执行sql
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                //释放资源
                JDBCUtils.closeResource(null,pst);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 通用查询:查询单行结果
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> T  queryForSigleInstance( Class<T> clazz, String sql , Object ...args){

        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i+1 , args[i]);
            }

            //执行sql,返回结果集
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                T t = (T) clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {

                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                return t;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);
        }


        return null;
    }

    /**
     * 通用查询:查询多行数据
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> List<T> queryForList(Class<T> clazz, String sql , Object ...args){
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i+1 , args[i]);
            }

            //执行sql,返回结果集
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            //创建集合对象
            ArrayList<T> list = new ArrayList<T>();

            while (rs.next()){
                T t =clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {

                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                list.add(t);
            }
            return  list;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);
        }


        return null;
    }



}
