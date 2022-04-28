package com.zhouge.dbutils;

import com.zhouge.statement.User;
import com.zhouge.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {

    @Test
    public void testInsert(){

        Connection conn = null ;

        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidPoolConnecion();
            String sql = "insert into t_user(username,password,balance) values(?,?,?)";
            int rows = runner.update(conn, sql, "EE","991227zg",3000);

            System.out.println(rows>=1 ? "添加成功":"添加失败");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }

    }

    @Test
    public void testDelete(){
        Connection conn = null ;

        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //执行sql并返回结果
            QueryRunner runner = new QueryRunner();
            String sql = "delete from t_user where username = ?";

            int rows = runner.update(conn, sql, "EE");

            //打印结果
            System.out.println(rows>=1?"删除成功":"删除失败");

        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            //释放资源
            JDBCUtils.closeResource(conn,null);

        }

    }

    @Test
    public void testUpdate(){
        Connection conn = null ;

        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //执行sql并返回结果
            QueryRunner runner = new QueryRunner();
            String sql = "update t_user set balance = ? where username = ?";

            int rows = runner.update(conn, sql, 2500 ,"DD");

            //打印结果
            System.out.println(rows>=1?"更新成功":"更新失败");

        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            //释放资源
            JDBCUtils.closeResource(conn,null);

        }

    }

    @Test
    public void testQueryOneRecord(){
        Connection conn = null ;

        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();

            //创建结果集处理对象
            BeanHandler<User> handler = new BeanHandler<>(User.class);

            //执行sql并返回结果
            String sql = "select username,password,balance from t_user where username = ?";
            User user = runner.query(conn, sql, handler, "AA");

            //输出结果
            System.out.println(user);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn,null);
        }

    }

    @Test
    public void testQueryAll(){
        Connection conn = null;

        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();

            //创建结果集处理对象
            BeanListHandler<User> handler = new BeanListHandler<>(User.class);

            //执行sql,并返回结果
            String sql = "select username,password,balance from t_user where balance < ?";
            List<User> list = runner.query(conn, sql, handler, 5000);

            //打印结果
            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }

    }

    @Test
    public void testMapHandler(){
        Connection conn = null ;
        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();

            //创建MapHandler对象
            MapHandler handler = new MapHandler();

            //执行sql,并返回结果
            String sql = "select username,password,balance from t_user where username = ?";
            Map<String, Object> map = runner.query(conn, sql, handler, "AA");

            //打印结果
            System.out.println(map);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    public void testMapListHandler(){
        Connection conn = null ;
        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //创建QueryRunner对象
            QueryRunner runner = new QueryRunner();

            //创建MapHandler对象
            MapListHandler handler = new MapListHandler();

            //执行sql,并返回结果
            String sql = "select username,password,balance from t_user where balance < ?";
            List<Map<String, Object>> list = runner.query(conn, sql, handler, 2000);

            //打印结果
            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @Test
    public void testScalarHandler(){
        Connection conn = null;

        try {
            //获取连接
            conn = JDBCUtils.getDruidPoolConnecion();

            //创建queryrunner对象
            QueryRunner runner = new QueryRunner();

            //创建scalerhander对象
            ScalarHandler handler = new ScalarHandler();

            //执行sql,并返回结果
            String sql = "select count(*) from t_user";
            long countNum = (long) runner.query(conn, sql, handler);

            //打印结果
            System.out.println(countNum);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }

    }


}
