package com.zhouge.Batch.method;

import com.zhouge.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestInsert {
    public static void main(String[] args) {
        new TestInsert().testPrepareStatementWithBatch();
    }

    /**
     * 原生PrepareStatement
     */
    public void testPrepareStatementForBatch(){
        Connection conn = null;
        PreparedStatement pst = null;
        int rows = -1 ;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql = "insert into t_goods values(null,?)";
            pst = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();

            //填充占位符
            for (int i = 0; i < 20000; i++) {
                pst.setObject(1,"name" + i+1);

                //执行sql
                rows = pst.executeUpdate();
            }

            long end = System.currentTimeMillis();
            System.out.println("花费的时间: " + (end - start));/* 花费的时间: 35338 */


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst);

            //打印结果
            System.out.println(rows >= 1 ? "执行成功":"执行失败");
        }



    }

    /**
     * addBatch() executeBatch()  clearBatch()
     */
    public void testPrepareStatementWithBatch(){
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql = "insert into t_goods values(null,?)";
            pst = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();

            //填充占位符
            for (int i = 1; i <= 20000; i++) {
                pst.setObject(1,"name_" + i);

                /* 默认情况下,MySQL不支持批处理
                * 需要将以下参数写在配置文件url后
                * ?rewriteBatchedStatements=true
                *  */

                //攒sql
                pst.addBatch();

                if(i % 500 == 0){
                    //执行batch
                    pst.executeBatch();

                    //清空batch
                    pst.clearBatch();
                }

            }

            long end = System.currentTimeMillis();
            System.out.println("花费的时间: " + (end - start));/* 花费的时间: 404 */


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst);
        }
    }

    /**
     * 事务批处理
     */
    public void testPrepareStatementWithTransaction(){
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //设置不自动提交数据
            conn.setAutoCommit(false);

            //预编译sql
            String sql = "insert into t_goods values(null,?)";
            pst = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();

            //填充占位符
            for (int i = 1; i <= 20000; i++) {
                pst.setObject(1,"name_" + i);

                /* 默认情况下,MySQL不支持批处理
                 * 需要将以下参数写在配置文件url后
                 * ?rewriteBatchedStatements=true
                 *  */

                //攒sql
                pst.addBatch();

                if(i % 500 == 0){
                    //执行batch
                    pst.executeBatch();

                    //清空batch
                    pst.clearBatch();
                }

            }

            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();
            System.out.println("花费的时间: " + (end - start));/* 花费的时间: 256 */


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放资源
            JDBCUtils.closeResource(conn,pst);
        }
    }


}
