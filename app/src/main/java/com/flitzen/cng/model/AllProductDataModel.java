package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProductDataModel {

    @SerializedName("data")
    @Expose
    private List<Result> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getResult() {
        return data;
    }

    public void setResult(List<Result> data) {
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

        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("subcategory")
        @Expose
        private List<Subcategory> subcategory = null;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Subcategory> getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(List<Subcategory> subcategory) {
            this.subcategory = subcategory;
        }

    }

    public class Product {

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

    }

    public class Subcategory {

        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("product")
        @Expose
        private List<Product> product = null;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Product> getProduct() {
            return product;
        }

        public void setProduct(List<Product> product) {
            this.product = product;
        }


    }
}
