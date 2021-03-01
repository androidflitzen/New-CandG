package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentListModel {
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_record")
    @Expose
    private String totalRecord;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
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

    public String getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(String totalRecord) {
        this.totalRecord = totalRecord;
    }

    public class Result {

        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("payment_id")
        @Expose
        private String paymentId;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("transaction_type")
        @Expose
        private String transactionType;
        @SerializedName("row_balance")
        @Expose
        private String rowBalance;
        @SerializedName("payment_reference")
        @Expose
        private String paymentReference;
        @SerializedName("sales_type")
        @Expose
        private String salesType;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
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

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getRowBalance() {
            return rowBalance;
        }

        public void setRowBalance(String rowBalance) {
            this.rowBalance = rowBalance;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public String getSalesType() {
            return salesType;
        }

        public void setSalesType(String salesType) {
            this.salesType = salesType;
        }

    }
}
