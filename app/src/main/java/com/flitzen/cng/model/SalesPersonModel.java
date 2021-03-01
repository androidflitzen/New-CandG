package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesPersonModel {
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

        @SerializedName("sales_person_id")
        @Expose
        private String salesPersonId;
        @SerializedName("name")
        @Expose
        private String name;

        public String getSalesPersonId() {
            return salesPersonId;
        }

        public void setSalesPersonId(String salesPersonId) {
            this.salesPersonId = salesPersonId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
