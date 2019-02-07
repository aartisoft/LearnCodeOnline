package com.example.serverfoodapp.Model;

import java.util.ArrayList;

public class Request {
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private ArrayList<Order> requestedOrderList;
    private int orderStatus;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Request(String name, String phone, String address, String total, ArrayList<Order> requestedOrderList) {
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

    public ArrayList<Order> getRequestedOrderList() {
        return requestedOrderList;
    }

    public void setRequestedOrderList(ArrayList<Order> requestedOrderList) {
        this.requestedOrderList = requestedOrderList;
    }
}
