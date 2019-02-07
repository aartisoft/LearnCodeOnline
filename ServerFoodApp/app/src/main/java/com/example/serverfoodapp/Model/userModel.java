package com.example.serverfoodapp.Model;

public class userModel {
    private String Name;
    private String Password;
    private long Contact;
    private boolean isStaff;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public userModel() {
    }

    public userModel(String userName, String userPassword, Long userContact) {
        Name = userName;
        Password = userPassword;
        Contact = userContact;

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

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }
}
