package com.zhouge.dao;

import com.zhouge.utils.JDBCUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {

    /**
     * 通用的增删改操作
     * @param conn
     * @param sql
     * @param args
     */
    public static void update(Connection conn , String sql , Object ... args){

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
     * 查询一条数据
     * @param conn
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> T  queryForSigleInstance(Connection conn , Class<T> clazz , String sql , Object ...args){
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
            JDBCUtils.closeResource(null,pst,rs);
        }


        return null;
    }

    /**
     * 查询多条数据
     * @param conn
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> List<T> queryForList(Connection conn , Class<T> clazz , String sql , Object ...args){
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
            JDBCUtils.closeResource(null,pst,rs);
        }


        return null;
    }

    /**
     * 查询单行单列数据
     * @param conn
     * @param sql
     * @param args
     * @param <E>
     * @return
     */
    public <E> E getSingleValue(Connection conn , String sql , Object ...args){
        PreparedStatement pst = null ;
        ResultSet rs = null ;

        try {
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i+1 , args[i]);
            }
            rs = pst.executeQuery();

            if(rs.next()){
                return (E) rs.getObject(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(null,pst,rs);
        }
        return  null;

    }


}
