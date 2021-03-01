package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    @SerializedName("data")
    @Expose
    private List<Result> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result {

        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("zero_vat")
        @Expose
        private String zeroVat;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("category_name")
        @Expose
        private String categoryName;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getZeroVat() {
            return zeroVat;
        }

        public void setZeroVat(String zeroVat) {
            this.zeroVat = zeroVat;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

    }
}
