package com.zhouge.prepareStatement;

import com.zhouge.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class CommonQuery {
    public static void main(String[] args) {
        String sql  = "select id,name,email,birth from t_customers where id = ?";
        //System.out.println(new CommonQuery().queryForSigleInstance(Customer.class, sql, 1));

        String sql2 = "select order_id orderId,order_name orderName,order_date orderDate from t_order where order_id = ?";
        //System.out.println(new CommonQuery().queryForSigleInstance(Order.class, sql2, 2));

        String sql3 = "select id,name,email,birth from t_customers where id < ?";
        List<Customer> customers = new CommonQuery().queryForList(Customer.class, sql3, 15);
        customers.forEach(System.out::println);

        String sql4 = "select order_id orderId,order_name orderName,order_date orderDate from t_order where order_id < ?";
        List<Order> orders = new CommonQuery().queryForList(Order.class, sql4, 5);
        orders.forEach(System.out::println);


    }

    /**
     * 通用查询:查询单行结果
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T  queryForSigleInstance( Class<T> clazz, String sql , Object ...args){

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
    public <T> List<T> queryForList(Class<T> clazz, String sql , Object ...args){
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
