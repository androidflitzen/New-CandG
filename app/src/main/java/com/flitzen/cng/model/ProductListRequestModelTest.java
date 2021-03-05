package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductListRequestModelTest implements Serializable {

    @SerializedName("api_key")
    @Expose
    private String api_key;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;
    @SerializedName("customer_name")
    @Expose
    private String customer_name;
    @SerializedName("sales_person_id")
    @Expose
    private String sales_person_id;
    @SerializedName("total_amount")
    @Expose
    private String total_amount;
    @SerializedName("vat_amount")
    @Expose
    private String vat_amount;
    @SerializedName("final_total")
    @Expose
    private String final_total;
    @SerializedName("sales_type")
    @Expose
    private String sales_type;
    @SerializedName("discount_amount")
    @Expose
    private String discount_amount;
    @SerializedName("purchase_no")
    @Expose
    private String purchase_no;
    @SerializedName("quotation_data")
    @Expose
    private List<ProductListRequestModel> quotation_data;

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getSales_person_id() {
        return sales_person_id;
    }

    public void setSales_person_id(String sales_person_id) {
        this.sales_person_id = sales_person_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(String vat_amount) {
        this.vat_amount = vat_amount;
    }

    public String getFinal_total() {
        return final_total;
    }

    public void setFinal_total(String final_total) {
        this.final_total = final_total;
    }

    public String getSales_type() {
        return sales_type;
    }

    public void setSales_type(String sales_type) {
        this.sales_type = sales_type;
    }

    public List<ProductListRequestModel> getQuotation_data() {
        return quotation_data;
    }

    public void setQuotation_data(List<ProductListRequestModel> quotation_data) {
        this.quotation_data = quotation_data;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getPurchase_no() {
        return purchase_no;
    }

    public void setPurchase_no(String purchase_no) {
        this.purchase_no = purchase_no;
    }
}
