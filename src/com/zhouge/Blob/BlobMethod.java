package com.zhouge.Blob;

import com.zhouge.prepareStatement.Customer;
import com.zhouge.utils.JDBCUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BlobMethod {

    public static void main(String[] args) {
        new BlobMethod().InsertBlob();
    }

    /**
     * 添加Blob数据
     */
    public void InsertBlob(){

        Connection conn = null;
        PreparedStatement pst = null;
        int rows = -1 ;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql = String.format("insert into t_customers(name,email,birth,photo) values(?,?,?,?)");
            pst = conn.prepareStatement(sql);


            //获取用户输入的值
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入用户:");
            String username = scanner.next();
            System.out.print("请输入邮箱:");
            String email = scanner.next();
            System.out.print("请输入生日:");
            String birth = scanner.next();

            //填充占位符
            pst.setString(1,username);
            pst.setString(2,email);
            pst.setString(3,birth);
            FileInputStream is = new FileInputStream(new File("libs/res/a.jpg"));
            pst.setBlob(4,is);

            //执行sql,并返回结果
            rows = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            JDBCUtils.closeResource(conn,pst);

            //输出结果
            System.out.println(rows >= 1 ?"执行成功":"执行失败");
        }


    }


    /**
     * 查询Blob数据
     */
    public void QueryBlob(){
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null ;
        InputStream is = null ;
        FileOutputStream fos = null;


        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            String sql = String.format("select id,name,email,birth,photo from t_customers where id = ?");
            pst = conn.prepareStatement(sql);


            //填充占位符
            pst.setInt(1,20);


            //执行sql
            rs = pst.executeQuery();

            //获取结果集
            if (rs.next()){

                //获取字段名的列值
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);

                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("libs/res/yuyating.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer) )!= -1){
                    fos.write(buffer,0,len);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //释放资源
            JDBCUtils.closeResource(conn,pst);
        }
    }


    public int Update(String sql , Object ...args){
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            //获取连接
            conn = JDBCUtils.getConnection();

            //预编译sql
            pst = conn.prepareStatement(sql);
            
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i+1,args[i]);
            }

            //执行sql,并返回结果
            int rows = pst.executeUpdate();

            return rows;

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            JDBCUtils.closeResource(conn,pst);

        }
        return 0 ;
    }


}
