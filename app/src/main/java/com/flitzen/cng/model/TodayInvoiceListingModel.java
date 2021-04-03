package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayInvoiceListingModel {

    @SerializedName("data")
    @Expose
    private List<Result> data = null;
    @SerializedName("total")
    @Expose
    private String total;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

        @SerializedName("invoice_id")
        @Expose
        private String invoiceId;
        @SerializedName("invoice_to")
        @Expose
        private String invoiceTo;
        @SerializedName("sales_person_name")
        @Expose
        private String salesPersonName;
        @SerializedName("invoice_date")
        @Expose
        private String invoiceDate;
        @SerializedName("invoice_time")
        @Expose
        private String invoiceTime;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("final_total")
        @Expose
        private String finalTotal;
        @SerializedName("purchase_no")
        @Expose
        private String purchaseNo;
        @SerializedName("sales_type")
        @Expose
        private String salesType;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("paid")
        @Expose
        private String paid;
        @SerializedName("due")
        @Expose
        private String due;
        @SerializedName("invoice_pdf_url")
        @Expose
        private String invoicePdfUrl;
        @SerializedName("invoice_delivery_note_pdf_url")
        @Expose
        private String invoiceDeliveryNotePdfUrl;
        @SerializedName("customer_email")
        @Expose
        private String customerEmail;

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getInvoiceTo() {
            return invoiceTo;
        }

        public void setInvoiceTo(String invoiceTo) {
            this.invoiceTo = invoiceTo;
        }

        public String getSalesPersonName() {
            return salesPersonName;
        }

        public void setSalesPersonName(String salesPersonName) {
            this.salesPersonName = salesPersonName;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public String getInvoiceTime() {
            return invoiceTime;
        }

        public void setInvoiceTime(String invoiceTime) {
            this.invoiceTime = invoiceTime;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(String vatAmount) {
            this.vatAmount = vatAmount;
        }

        public String getFinalTotal() {
            return finalTotal;
        }

        public void setFinalTotal(String finalTotal) {
            this.finalTotal = finalTotal;
        }

        public String getPurchaseNo() {
            return purchaseNo;
        }

        public void setPurchaseNo(String purchaseNo) {
            this.purchaseNo = purchaseNo;
        }

        public String getSalesType() {
            return salesType;
        }

        public void setSalesType(String salesType) {
            this.salesType = salesType;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getPaid() {
            return paid;
        }

        public void setPaid(String paid) {
            this.paid = paid;
        }

        public String getDue() {
            return due;
        }

        public void setDue(String due) {
            this.due = due;
        }

        public String getInvoicePdfUrl() {
            return invoicePdfUrl;
        }

        public void setInvoicePdfUrl(String invoicePdfUrl) {
            this.invoicePdfUrl = invoicePdfUrl;
        }

        public String getInvoiceDeliveryNotePdfUrl() {
            return invoiceDeliveryNotePdfUrl;
        }

        public void setInvoiceDeliveryNotePdfUrl(String invoiceDeliveryNotePdfUrl) {
            this.invoiceDeliveryNotePdfUrl = invoiceDeliveryNotePdfUrl;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }
    }
}
