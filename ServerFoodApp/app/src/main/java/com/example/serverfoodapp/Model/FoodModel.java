package com.example.serverfoodapp.Model;

import com.google.firebase.database.PropertyName;

public class FoodModel {
    private String Description;
    private String Discount;
    private String Image;
    private String MenuId;
    private String Name;
    private String Price;

    public FoodModel() {
    }

    public FoodModel(String description, String discount, String image, String menuId, String name, String price) {
        Description = description;
        Discount = discount;
        Image = image;
        MenuId = menuId;
        Name = name;
        Price = price;
    }

    @PropertyName("Description")
    public String getDescription() {
        return Description;
    }

    @PropertyName("Description")
    public void setDescription(String description) {
        Description = description;
    }

    @PropertyName("Discount")
    public String getDiscount() {
        return Discount;
    }

    @PropertyName("Discount")
    public void setDiscount(String discount) {
        Discount = discount;
    }

    @PropertyName("Image")
    public String getImage() {
        return Image;
    }

    @PropertyName("Image")
    public void setImage(String image) {
        Image = image;
    }

    @PropertyName("MenuId")
    public String getMenuId() {
        return MenuId;
    }

    @PropertyName("MenuId")
    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    @PropertyName("Name")
    public String getName() {
        return Name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }

    @PropertyName("Price")
    public String getPrice() {
        return Price;
    }

    @PropertyName("Price")
    public void setPrice(String price) {
        Price = price;
    }
}
