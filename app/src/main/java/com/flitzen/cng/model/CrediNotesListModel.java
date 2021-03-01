package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrediNotesListModel {
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

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

    public class Result {

        @SerializedName("credit_note_id")
        @Expose
        private String creditNoteId;
        @SerializedName("credit_unique_id")
        @Expose
        private String creditUniqueId;
        @SerializedName("credit_to")
        @Expose
        private String creditTo;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("sales_person")
        @Expose
        private String salesPerson;
        @SerializedName("sales_id")
        @Expose
        private String salesId;
        @SerializedName("credit_note_date")
        @Expose
        private String creditNoteDate;
        @SerializedName("credit_note_time")
        @Expose
        private String creditNoteTime;
        @SerializedName("pdf_url")
        @Expose
        private String pdfUrl;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("po_number")
        @Expose
        private String poNumber;

        public String getCreditNoteId() {
            return creditNoteId;
        }

        public void setCreditNoteId(String creditNoteId) {
            this.creditNoteId = creditNoteId;
        }

        public String getCreditUniqueId() {
            return creditUniqueId;
        }

        public void setCreditUniqueId(String creditUniqueId) {
            this.creditUniqueId = creditUniqueId;
        }

        public String getCreditTo() {
            return creditTo;
        }

        public void setCreditTo(String creditTo) {
            this.creditTo = creditTo;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getSalesPerson() {
            return salesPerson;
        }

        public void setSalesPerson(String salesPerson) {
            this.salesPerson = salesPerson;
        }

        public String getSalesId() {
            return salesId;
        }

        public void setSalesId(String salesId) {
            this.salesId = salesId;
        }

        public String getCreditNoteDate() {
            return creditNoteDate;
        }

        public void setCreditNoteDate(String creditNoteDate) {
            this.creditNoteDate = creditNoteDate;
        }

        public String getCreditNoteTime() {
            return creditNoteTime;
        }

        public void setCreditNoteTime(String creditNoteTime) {
            this.creditNoteTime = creditNoteTime;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

        public String getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(String vatAmount) {
            this.vatAmount = vatAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

    }

}
