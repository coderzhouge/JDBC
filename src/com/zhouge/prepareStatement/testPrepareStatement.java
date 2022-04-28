package com.zhouge.prepareStatement;

import com.zhouge.utils.JDBCUtils;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class testPrepareStatement {

    public static void main(String[] args){
       String sql = "select order_id orderId,order_name orderName,order_date orderDate from t_order where order_id = ?";
       System.out.println(new testPrepareStatement().queryForOrder(sql, 1));

    }

    /**
     * 测试添加操作
     */
    public void testInsert() throws Exception {
        //读取配置文件信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties info = new Properties();
        info.load(is);

        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String url2 = info.getProperty("url2");
        String driverClass = info.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connection = DriverManager.getConnection(url2, user, password);

        //执行sql
        String sql = "insert into t_customers(name,email,birth) values(?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);

        //填充占位符
        pst.setString(1,"哪吒");
        pst.setString(2,"nazha@gmail.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("1997-01-01");
        pst.setDate(3, new java.sql.Date(date.getTime()));

        pst.execute();

        //释放资源
        pst.close();
        connection.close();

    }

    /**
     * 测试增删改
     */
    public void testUpdate() {

        Connection connection = null;
        PreparedStatement pst = null;

        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();

            //预编译sql语句
            String sql = "update t_customers set name = ? where id = ?";
            pst = connection.prepareStatement(sql);

            //填充占位符
            pst.setObject(1,"莫扎特");
            pst.setObject(2,18);

            //执行sql语句
            int rows = pst.executeUpdate();

            System.out.println(rows >=1 ? "执行成功":"执行失败");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //资源关闭
            JDBCUtils.closeResource(connection,pst);
        }



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
     * 测试查询
     */
    public void testForQuery(){

        Connection conn = null ;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql =  "select id,name,email,birth from t_customers where id = ?";
            pst = conn.prepareStatement(sql);

            //填充占位符
            pst.setObject(1,1);

            //执行sql,返回结果集
            rs = pst.executeQuery();

            if(rs.next()){

                //获取结果集字段及数据
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                java.sql.Date birth = rs.getDate(4);


                //获取字段数据
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);
        }

    }

    /**
     * 针对Customers表的通用查询
     * @return
     */
    public Customer queryForCustomers(String sql ,Object ...args) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null ;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i+1,args[i]);
            }


            //执行sql,获取结果集
            rs = pst.executeQuery();

            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集列数
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                Customer customer = new Customer();

                for (int i = 0; i < columnCount; i++) {
                    //获取第i+1列的数据
                    Object columnValue = rs.getObject(i + 1);

                    //获取每列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //通过反射给执行对象的columnName属性,赋值为columnValue
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);//防止属性私有,将属性设置为公开
                    field.set(customer,columnValue);

                }

                return customer;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);
        }

        return  null;
    }

    /**
     * 测试Order表
     *
     * 针对表的字段名与类的属性名不相同的情况:
     *  1声明sql时,必须使用类的属性名类命名字段的别名.(即,类的属性名和表字段的别名必须一致)
     *  2使用ResultSetMetaData时,需要使用getColumnLabel() 来替换 getColumnName() 来获取列的别名
     *          说明:如果sql中没有给字段起别名,getColumnLabel() 获取的就是列名
     */
    public void testForQuery2(){
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;


        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql = "select order_id,order_name,order_date from t_order where order_id = ?";
            pst = conn.prepareStatement(sql);

            //填充占位符
            pst.setObject(1,1);

            //获取结果集
            rs = pst.executeQuery();
            Order order = null ;
            if(rs.next()){
                int orderId = (int) rs.getObject(1);
                String orderName = (String) rs.getObject(2);
                java.sql.Date orderDate = (java.sql.Date) rs.getObject(3);

                order = new Order(orderId, orderName, orderDate);
            }
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);

        }

    }

    /**
     * 针对Order表的通用查询
     * @return
     */
    public Order queryForOrder(String sql , Object ... args){


        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject( i + 1,args[i]);
            }

            //执行sql,返回结果集
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();//列数

            if(rs.next()){
                Order order = new Order();

                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i+1);

                    //获取列名
                    String columnLabel = rsmd.getColumnLabel(i+1);

                    //通过反射给Order指定的属性columnLable 赋值columnValue
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);

                }

                return  order;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            JDBCUtils.closeResource(conn,pst,rs);
        }

        return null;
    }


}
