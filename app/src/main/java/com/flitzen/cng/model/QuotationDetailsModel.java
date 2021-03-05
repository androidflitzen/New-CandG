package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuotationDetailsModel {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public class QuotationProductData {

        @SerializedName("quotation_id")
        @Expose
        private String quotationId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("product_price")
        @Expose
        private String productPrice;
        @SerializedName("product_qty")
        @Expose
        private String productQty;
        @SerializedName("product_discount_percentage")
        @Expose
        private String productDiscountPercentage;
        @SerializedName("product_final_price")
        @Expose
        private String productFinalPrice;
        @SerializedName("product_subcategory_id")
        @Expose
        private String productSubcategoryId;
        @SerializedName("product_category_id")
        @Expose
        private String productCategoryId;
        @SerializedName("product_category_name")
        @Expose
        private String productCategoryName;
        @SerializedName("zero_vat")
        @Expose
        private String zeroVat;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_subcategory_name")
        @Expose
        private String productSubcategoryName;

        public String getQuotationId() {
            return quotationId;
        }

        public void setQuotationId(String quotationId) {
            this.quotationId = quotationId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductQty() {
            return productQty;
        }

        public void setProductQty(String productQty) {
            this.productQty = productQty;
        }

        public String getProductDiscountPercentage() {
            return productDiscountPercentage;
        }

        public void setProductDiscountPercentage(String productDiscountPercentage) {
            this.productDiscountPercentage = productDiscountPercentage;
        }

        public String getProductFinalPrice() {
            return productFinalPrice;
        }

        public void setProductFinalPrice(String productFinalPrice) {
            this.productFinalPrice = productFinalPrice;
        }

        public String getProductSubcategoryId() {
            return productSubcategoryId;
        }

        public void setProductSubcategoryId(String productSubcategoryId) {
            this.productSubcategoryId = productSubcategoryId;
        }

        public String getProductCategoryId() {
            return productCategoryId;
        }

        public void setProductCategoryId(String productCategoryId) {
            this.productCategoryId = productCategoryId;
        }

        public String getProductCategoryName() {
            return productCategoryName;
        }

        public void setProductCategoryName(String productCategoryName) {
            this.productCategoryName = productCategoryName;
        }

        public String getZeroVat() {
            return zeroVat;
        }

        public void setZeroVat(String zeroVat) {
            this.zeroVat = zeroVat;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSubcategoryName() {
            return productSubcategoryName;
        }

        public void setProductSubcategoryName(String productSubcategoryName) {
            this.productSubcategoryName = productSubcategoryName;
        }

    }

    public class Data {

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
        @SerializedName("sales_person_id")
        @Expose
        private String salesPersonId;
        @SerializedName("sales_type")
        @Expose
        private String salesType;
        @SerializedName("purchase_no")
        @Expose
        private String purchaseNo;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("zero_vat_total")
        @Expose
        private String zeroVatTotal;
        @SerializedName("quotation_data")
        @Expose
        private List<QuotationProductData> quotationData = null;

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

        public String getSalesPersonId() {
            return salesPersonId;
        }

        public void setSalesPersonId(String salesPersonId) {
            this.salesPersonId = salesPersonId;
        }

        public String getSalesType() {
            return salesType;
        }

        public void setSalesType(String salesType) {
            this.salesType = salesType;
        }

        public String getPurchaseNo() {
            return purchaseNo;
        }

        public void setPurchaseNo(String purchaseNo) {
            this.purchaseNo = purchaseNo;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getZeroVatTotal() {
            return zeroVatTotal;
        }

        public void setZeroVatTotal(String zeroVatTotal) {
            this.zeroVatTotal = zeroVatTotal;
        }

        public List<QuotationProductData> getQuotationData() {
            return quotationData;
        }

        public void setQuotationData(List<QuotationProductData> quotationData) {
            this.quotationData = quotationData;
        }

    }

}
