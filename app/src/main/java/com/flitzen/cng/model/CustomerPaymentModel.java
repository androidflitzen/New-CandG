package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerPaymentModel {
    @SerializedName("payments")
    @Expose
    private List<Payment> payments = null;
    @SerializedName("closing_balance")
    @Expose
    private String closingBalance;
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
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

    public class Payment {

        @SerializedName("payment_id")
        @Expose
        private String paymentId;
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate;
        @SerializedName("transaction_type")
        @Expose
        private String transactionType;
        @SerializedName("cr/dr")
        @Expose
        private String crDr;
        @SerializedName("transaction_mode")
        @Expose
        private String transactionMode;
        @SerializedName("payment_reference")
        @Expose
        private String paymentReference;
        @SerializedName("amount")
        @Expose
        private String amount;

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getCrDr() {
            return crDr;
        }

        public void setCrDr(String crDr) {
            this.crDr = crDr;
        }

        public String getTransactionMode() {
            return transactionMode;
        }

        public void setTransactionMode(String transactionMode) {
            this.transactionMode = transactionMode;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

}
