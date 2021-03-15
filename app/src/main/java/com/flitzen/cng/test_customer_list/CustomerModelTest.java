package com.flitzen.cng.test_customer_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerModelTest {
    private String customerId;
    private String name;
    private String phone_no;
    private String type;

    public CustomerModelTest(String customerId, String name, String phone_no, String type) {
        this.customerId = customerId;
        this.name = name;
        this.phone_no = phone_no;
        this.type = type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
