package com.example.shikharjai.foodapp.Model;

public class CategoryModel {
    private String Image;
    private String Name;

    public CategoryModel() {
    }
    public CategoryModel(String Image, String name) {

        this.Image = Image;
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
