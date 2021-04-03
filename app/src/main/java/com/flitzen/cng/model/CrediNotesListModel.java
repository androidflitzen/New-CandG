package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrediNotesListModel {
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

        @SerializedName("creadit_note_id")
        @Expose
        private String creaditNoteId;
        @SerializedName("credit_note_to")
        @Expose
        private String creditNoteTo;
        @SerializedName("sales_person_name")
        @Expose
        private String salesPersonName;
        @SerializedName("creadit_note_date")
        @Expose
        private String creaditNoteDate;
        @SerializedName("creadit_note_time")
        @Expose
        private String creaditNoteTime;
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
        private String purchase_no;
        @SerializedName("credit_note_pdf_url")
        @Expose
        private String creditNotePdfUrl;
        @SerializedName("customer_email")
        @Expose
        private String customerEmail;

        public String getPurchase_no() {
            return purchase_no;
        }

        public void setPurchase_no(String purchase_no) {
            this.purchase_no = purchase_no;
        }

        public String getCreaditNoteId() {
            return creaditNoteId;
        }

        public void setCreaditNoteId(String creaditNoteId) {
            this.creaditNoteId = creaditNoteId;
        }

        public String getCreditNoteTo() {
            return creditNoteTo;
        }

        public void setCreditNoteTo(String creditNoteTo) {
            this.creditNoteTo = creditNoteTo;
        }

        public String getSalesPersonName() {
            return salesPersonName;
        }

        public void setSalesPersonName(String salesPersonName) {
            this.salesPersonName = salesPersonName;
        }

        public String getCreaditNoteDate() {
            return creaditNoteDate;
        }

        public void setCreaditNoteDate(String creaditNoteDate) {
            this.creaditNoteDate = creaditNoteDate;
        }

        public String getCreaditNoteTime() {
            return creaditNoteTime;
        }

        public void setCreaditNoteTime(String creaditNoteTime) {
            this.creaditNoteTime = creaditNoteTime;
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

        public String getCreditNotePdfUrl() {
            return creditNotePdfUrl;
        }

        public void setCreditNotePdfUrl(String creditNotePdfUrl) {
            this.creditNotePdfUrl = creditNotePdfUrl;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }
    }

}
