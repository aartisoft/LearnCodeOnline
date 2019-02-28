package com.example.serverfoodapp.Common;


import com.example.serverfoodapp.Model.Request;
import com.example.serverfoodapp.Model.userModel;

public class User {
    public static userModel currentUser;
    public static Request currentRequest;
    public static final String baseUrl = "https://maps.googleapis.com";
    public static final String apiKey = "AIzaSyDS2FC8-l6UwOwZ2XZvYQZQwVUJdbHAOWY";

    public static String convertToStatus(int order_status) {
        String str;
        switch (order_status){
            case 0 : str = "Placed";
                break;
            case 1 : str = "Shipped";
                break;
            case 2 : str = "Reached to Recepient's City";
                break;
            case 3 : str = "Delivered";
                break;
            default: str = "Pending";
                break;
        }
        return str;
    }

    public static int convertStatusToDigit(String order_status) {
        int str;
        switch (order_status){
            case "Placed" : str = 0;
                break;
            case "Shipped" : str = 1;
                break;
            case "Reached to Recepient's City" : str = 2;
                break;
            case "Delivered" : str = 3;
                break;
            default: str = 5;
                break;
        }
        return str;
    }

}

