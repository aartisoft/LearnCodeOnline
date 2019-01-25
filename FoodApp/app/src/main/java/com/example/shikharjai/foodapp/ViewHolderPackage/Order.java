package com.example.shikharjai.foodapp.ViewHolderPackage;

public class Order {
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


}
