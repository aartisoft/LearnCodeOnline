package com.example.serverfoodapp.Model;

import com.google.firebase.database.PropertyName;

public class CategoryModel {
    private String Image;
    private String Name;

    public CategoryModel(String image, String name) {
        Image = image;
        Name = name;
    }

    public CategoryModel() {
    }

    @PropertyName("Image")
    public String getImage() {
        return Image;
    }

    @PropertyName("Image")
    public void setImage(String image) {
        Image = image;
    }

    @PropertyName("Name")
    public String getName() {
        return Name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }
}