package com.example.shikharjai.foodapp.Model;

public class TimeLineModel {
    private String message;
    private String date;
    private String orderStatus;

    public TimeLineModel(String message, String date, String orderStatus) {
        this.message = message;
        this.date = date;
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
