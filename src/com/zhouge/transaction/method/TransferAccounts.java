package com.zhouge.transaction.method;

import com.zhouge.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.SQLException;

public class TransferAccounts {

    /**
     * 转账业务演示
     */
    public void demo() {

        Connection conn = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //取消事务自动提交
            conn.setAutoCommit(false);

            //模拟网络异常
            //System.out.println(10/0);

            //预编译sql
            String sql1 = "update t_user set balance = (balance + 100) where username = 'AA';";
            String sql2 = "update t_user set balance = (balance - 100) where username = 'BB';";

            //执行sql
            JDBCUtils.update(conn,sql1);
            JDBCUtils.update(conn,sql2);

            //提交事务
            conn.commit();

            //提示
            System.out.println("转账成功");

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("转账失败");

            try {
                //如果执行有误,则回滚事务
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }finally{

            try {
                //设置事务为默认提交状态
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //关闭连接
            JDBCUtils.closeResource(conn,null);

        }


    }

}
