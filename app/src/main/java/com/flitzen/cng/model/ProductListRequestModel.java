package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListRequestModel {

    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_price")
    @Expose
    private String product_price;
    @SerializedName("product_qty")
    @Expose
    private String product_qty;
    @SerializedName("product_discount_percentage")
    @Expose
    private String product_discount_percentage;
    @SerializedName("product_final_price")
    @Expose
    private String product_final_price;


    public ProductListRequestModel(String product_id, String product_price, String product_qty, String product_discount_percentage, String product_final_price) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.product_qty = product_qty;
        this.product_discount_percentage = product_discount_percentage;
        this.product_final_price = product_final_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_discount_percentage() {
        return product_discount_percentage;
    }

    public void setProduct_discount_percentage(String product_discount_percentage) {
        this.product_discount_percentage = product_discount_percentage;
    }

    public String getProduct_final_price() {
        return product_final_price;
    }

    public void setProduct_final_price(String product_final_price) {
        this.product_final_price = product_final_price;
    }
}
