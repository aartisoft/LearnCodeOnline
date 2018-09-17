package com.example.shikharjai.contactsyn;

public class Contact {
    String contact_Uid;
    String contact_name;
    String contact_number;
    String contact_email;
    String contact_add;

    public Contact(String contact_Uid, String contact_name, String contact_number, String contact_email, String contact_add) {
        this.contact_Uid = contact_Uid;
        this.contact_name = contact_name;
        this.contact_email = contact_email;
        this.contact_number = contact_number;
        this.contact_add = contact_add;
    }

    public String getContact_Uid() {
        return contact_Uid;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getContact_add() {
        return contact_add;
    }
}
