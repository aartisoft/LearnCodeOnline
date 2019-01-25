package com.example.shikharjai.foodapp.Model;

import com.example.shikharjai.foodapp.ViewHolderPackage.Order;

import java.util.List;

public class Request {
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private List<Order> requestedOrderList;
    private int orderStatus;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Request(String name, String phone, String address, String total, List<Order> requestedOrderList) {
        Name = name;
        Phone = phone;
        Address = address;
        Total = total;
        this.requestedOrderList = requestedOrderList;
        orderStatus =0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getRequestedOrderList() {
        return requestedOrderList;
    }

    public void setRequestedOrderList(List<Order> requestedOrderList) {
        this.requestedOrderList = requestedOrderList;
    }
}
