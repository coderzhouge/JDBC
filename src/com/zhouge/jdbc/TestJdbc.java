package com.zhouge.jdbc;

public class TestJdbc {
    public static void main(String[] args) {
        testMysql();
        System.out.println("====================");
        testOracle();

    }

    /**
     * 测试mysql
     */
    public static void testMysql(){
        MysqlJdbcImpl jdbc = new MysqlJdbcImpl();
        jdbc.getConnection();
        jdbc.curd();
        jdbc.close();
    }

    /**
     * 测试oracle
     */
    public static void testOracle(){
        OracleJdbcImpl oracle = new OracleJdbcImpl();
        oracle.getConnection();
        oracle.curd();
        oracle.close();

    }
}
