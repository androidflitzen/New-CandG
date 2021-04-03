package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerLedgerModel {
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
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

    public class Data {

        @SerializedName("is_cr_dr")
        @Expose
        private String isCrDr;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("reference")
        @Expose
        private String reference;

        public String getIsCrDr() {
            return isCrDr;
        }

        public void setIsCrDr(String isCrDr) {
            this.isCrDr = isCrDr;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

    }
}
