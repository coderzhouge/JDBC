package com.zhouge.prepareStatement;

import java.sql.Date;

/**
 * 针对表的字段名与类的属性名不相同的情况:
 *    1声明sql时,必须使用类的属性名类命名字段的别名.(即,类的属性名和表字段的别名必须一致)
 *    2使用ResultSetMetaData时,需要使用getColumnLabel() 来替换 getColumnName() 来获取列的别名
 *      说明:如果sql中没有给字段起别名,getColumnLabel() 获取的就是列名
 */
public class Order {

    private int orderId ;
    private String orderName ;
    private Date orderDate ;

    public Order() {
    }

    public Order(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
