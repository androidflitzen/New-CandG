package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuotationListingModel {
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

        @SerializedName("quotation_id")
        @Expose
        private String quotationId;
        @SerializedName("quotation_to")
        @Expose
        private String quotationTo;
        @SerializedName("sales_person_name")
        @Expose
        private String salesPersonName;
        @SerializedName("quotation_date")
        @Expose
        private String quotationDate;
        @SerializedName("quotation_time")
        @Expose
        private String quotationTime;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("final_total")
        @Expose
        private String finalTotal;
        @SerializedName("converted_status")
        @Expose
        private String convertedStatus;

        public String getQuotationId() {
            return quotationId;
        }

        public void setQuotationId(String quotationId) {
            this.quotationId = quotationId;
        }

        public String getQuotationTo() {
            return quotationTo;
        }

        public void setQuotationTo(String quotationTo) {
            this.quotationTo = quotationTo;
        }

        public String getSalesPersonName() {
            return salesPersonName;
        }

        public void setSalesPersonName(String salesPersonName) {
            this.salesPersonName = salesPersonName;
        }

        public String getQuotationDate() {
            return quotationDate;
        }

        public void setQuotationDate(String quotationDate) {
            this.quotationDate = quotationDate;
        }

        public String getQuotationTime() {
            return quotationTime;
        }

        public void setQuotationTime(String quotationTime) {
            this.quotationTime = quotationTime;
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

        public String getConvertedStatus() {
            return convertedStatus;
        }

        public void setConvertedStatus(String convertedStatus) {
            this.convertedStatus = convertedStatus;
        }
    }
    }
