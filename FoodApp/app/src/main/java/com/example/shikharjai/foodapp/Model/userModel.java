package com.example.shikharjai.foodapp.Model;

import android.content.ContentValues;

public class userModel {
    private String Name;
    private String Password;
    private long Contact;
    private boolean isStaff;
    private String userName;

   public userModel(String userName, String userPassword, Long userContact) {
        Name = userName;
        Password = userPassword;
        Contact = userContact;
        isStaff = false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public userModel() {
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public long getContact() {
        return Contact;
    }

    public void setContact(long contact) {
        Contact = contact;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }
}
