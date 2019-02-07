package com.example.serverfoodapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private String ProductId;
    private String ProductName;
    private String ProductPrice;
    private String ProductDiscount;
    private String ProductQuantity;

    public Order(String productId, String productName, String productPrice, String productDiscount, String productQuantity) {
        ProductId = productId;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductDiscount = productDiscount;
        ProductQuantity = productQuantity;
    }

    public Order() {
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductDiscount() {
        return ProductDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        ProductDiscount = productDiscount;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    public Order(Parcel source) {
        ProductId = source.readString();
        ProductName = source.readString();
        ProductPrice = source.readString();
        ProductDiscount = source.readString();
        ProductQuantity= source.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ProductId);
        dest.writeString(ProductName);
        dest.writeString(ProductPrice);
        dest.writeString(ProductDiscount);
        dest.writeString(ProductQuantity);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

}
