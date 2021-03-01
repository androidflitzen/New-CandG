package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerInvoiceListModel {

    public class CreditNote {

        @SerializedName("credit_note_id")
        @Expose
        private String creditNoteId;
        @SerializedName("purchase_no")
        @Expose
        private String purchaseNo;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("credit_note_dt")
        @Expose
        private String creditNoteDt;
        @SerializedName("credit_note_time")
        @Expose
        private String creditNoteTime;
        @SerializedName("sales_person")
        @Expose
        private String salesPerson;
        @SerializedName("sales_id")
        @Expose
        private String salesId;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("pdf_url")
        @Expose
        private String pdfUrl;

        public String getCreditNoteId() {
            return creditNoteId;
        }

        public void setCreditNoteId(String creditNoteId) {
            this.creditNoteId = creditNoteId;
        }

        public String getPurchaseNo() {
            return purchaseNo;
        }

        public void setPurchaseNo(String purchaseNo) {
            this.purchaseNo = purchaseNo;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getCreditNoteDt() {
            return creditNoteDt;
        }

        public void setCreditNoteDt(String creditNoteDt) {
            this.creditNoteDt = creditNoteDt;
        }

        public String getCreditNoteTime() {
            return creditNoteTime;
        }

        public void setCreditNoteTime(String creditNoteTime) {
            this.creditNoteTime = creditNoteTime;
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

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

    }

    public class Statement {

        @SerializedName("month")
        @Expose
        private String month;
        @SerializedName("closing_balance")
        @Expose
        private String closingBalance;
        @SerializedName("closing_balance_type")
        @Expose
        private String closingBalanceType;
        @SerializedName("pdf_url")
        @Expose
        private String pdfUrl;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getClosingBalance() {
            return closingBalance;
        }

        public void setClosingBalance(String closingBalance) {
            this.closingBalance = closingBalance;
        }

        public String getClosingBalanceType() {
            return closingBalanceType;
        }

        public void setClosingBalanceType(String closingBalanceType) {
            this.closingBalanceType = closingBalanceType;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

    }

    public class Quotation {

        @SerializedName("quotation_id")
        @Expose
        private String quotationId;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("quotation_dt")
        @Expose
        private String quotationDt;
        @SerializedName("quotation_time")
        @Expose
        private String quotationTime;
        @SerializedName("quotatoin_to")
        @Expose
        private String quotatoinTo;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("sales_person")
        @Expose
        private String salesPerson;
        @SerializedName("sales_id")
        @Expose
        private String salesId;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("converted_status")
        @Expose
        private String convertedStatus;
        @SerializedName("pdf_url")
        @Expose
        private String pdfUrl;

        public String getQuotationId() {
            return quotationId;
        }

        public void setQuotationId(String quotationId) {
            this.quotationId = quotationId;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getQuotationDt() {
            return quotationDt;
        }

        public void setQuotationDt(String quotationDt) {
            this.quotationDt = quotationDt;
        }

        public String getQuotationTime() {
            return quotationTime;
        }

        public void setQuotationTime(String quotationTime) {
            this.quotationTime = quotationTime;
        }

        public String getQuotatoinTo() {
            return quotatoinTo;
        }

        public void setQuotatoinTo(String quotatoinTo) {
            this.quotatoinTo = quotatoinTo;
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

        public String getConvertedStatus() {
            return convertedStatus;
        }

        public void setConvertedStatus(String convertedStatus) {
            this.convertedStatus = convertedStatus;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

    }

    public class ProfileDetail {

        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("customer_category")
        @Expose
        private String customerCategory;
        @SerializedName("phone_number")
        @Expose
        private String phoneNumber;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("post_code")
        @Expose
        private String postCode;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("account_managers")
        @Expose
        private String accountManagers;
        @SerializedName("credit_days")
        @Expose
        private String creditDays;
        @SerializedName("credit_limit")
        @Expose
        private String creditLimit;
        @SerializedName("closing_balance")
        @Expose
        private String closingBalance;
        @SerializedName("closing_balance_type")
        @Expose
        private String closingBalanceType;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCustomerCategory() {
            return customerCategory;
        }

        public void setCustomerCategory(String customerCategory) {
            this.customerCategory = customerCategory;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAccountManagers() {
            return accountManagers;
        }

        public void setAccountManagers(String accountManagers) {
            this.accountManagers = accountManagers;
        }

        public String getCreditDays() {
            return creditDays;
        }

        public void setCreditDays(String creditDays) {
            this.creditDays = creditDays;
        }

        public String getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(String creditLimit) {
            this.creditLimit = creditLimit;
        }

        public String getClosingBalance() {
            return closingBalance;
        }

        public void setClosingBalance(String closingBalance) {
            this.closingBalance = closingBalance;
        }

        public String getClosingBalanceType() {
            return closingBalanceType;
        }

        public void setClosingBalanceType(String closingBalanceType) {
            this.closingBalanceType = closingBalanceType;
        }

    }

    public class Payment {

        @SerializedName("payment_id")
        @Expose
        private String paymentId;
        @SerializedName("invoice_id")
        @Expose
        private String invoiceId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("payment_date")
        @Expose
        private String paymentDate;
        @SerializedName("payment_reference")
        @Expose
        private String paymentReference;
        @SerializedName("payment_note")
        @Expose
        private String paymentNote;

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public String getPaymentNote() {
            return paymentNote;
        }

        public void setPaymentNote(String paymentNote) {
            this.paymentNote = paymentNote;
        }

    }

    public class Invoice {

        @SerializedName("invoice_id")
        @Expose
        private String invoiceId;
        @SerializedName("unique_id")
        @Expose
        private String uniqueId;
        @SerializedName("invoice_to")
        @Expose
        private String invoiceTo;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("tot_amount")
        @Expose
        private String totAmount;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("sales_person")
        @Expose
        private String salesPerson;
        @SerializedName("sales_id")
        @Expose
        private String salesId;
        @SerializedName("tot_paid")
        @Expose
        private String totPaid;
        @SerializedName("invoice_date")
        @Expose
        private String invoiceDate;
        @SerializedName("invoice_time")
        @Expose
        private String invoiceTime;
        @SerializedName("pdf_url")
        @Expose
        private String pdfUrl;
        @SerializedName("url_delivery_note")
        @Expose
        private String urlDeliveryNote;
        @SerializedName("pending_amount")
        @Expose
        private String pendingAmount;
        @SerializedName("sub_total")
        @Expose
        private String subTotal;
        @SerializedName("vat_amount")
        @Expose
        private String vatAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("is_credit_note")
        @Expose
        private Integer isCreditNote;
        @SerializedName("credit_note_url")
        @Expose
        private String creditNoteUrl;
        @SerializedName("sales_type")
        @Expose
        private String salesType;
        @SerializedName("purchase_no")
        @Expose
        private String purchaseNo;

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getInvoiceTo() {
            return invoiceTo;
        }

        public void setInvoiceTo(String invoiceTo) {
            this.invoiceTo = invoiceTo;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getTotAmount() {
            return totAmount;
        }

        public void setTotAmount(String totAmount) {
            this.totAmount = totAmount;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
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

        public String getTotPaid() {
            return totPaid;
        }

        public void setTotPaid(String totPaid) {
            this.totPaid = totPaid;
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

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

        public String getUrlDeliveryNote() {
            return urlDeliveryNote;
        }

        public void setUrlDeliveryNote(String urlDeliveryNote) {
            this.urlDeliveryNote = urlDeliveryNote;
        }

        public String getPendingAmount() {
            return pendingAmount;
        }

        public void setPendingAmount(String pendingAmount) {
            this.pendingAmount = pendingAmount;
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

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Integer getIsCreditNote() {
            return isCreditNote;
        }

        public void setIsCreditNote(Integer isCreditNote) {
            this.isCreditNote = isCreditNote;
        }

        public String getCreditNoteUrl() {
            return creditNoteUrl;
        }

        public void setCreditNoteUrl(String creditNoteUrl) {
            this.creditNoteUrl = creditNoteUrl;
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

    }

    public class Example {

        @SerializedName("statement")
        @Expose
        private List<Statement> statement = null;
        @SerializedName("final_balance")
        @Expose
        private String finalBalance;
        @SerializedName("final_balance_type")
        @Expose
        private String finalBalanceType;
        @SerializedName("color_code")
        @Expose
        private String colorCode;
        @SerializedName("profile_details")
        @Expose
        private List<ProfileDetail> profileDetails = null;
        @SerializedName("invoices")
        @Expose
        private List<Invoice> invoices = null;
        @SerializedName("payments")
        @Expose
        private List<Payment> payments = null;
        @SerializedName("quotations")
        @Expose
        private List<Quotation> quotations = null;
        @SerializedName("credit_note")
        @Expose
        private List<CreditNote> creditNote = null;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("message")
        @Expose
        private String message;

        public List<Statement> getStatement() {
            return statement;
        }

        public void setStatement(List<Statement> statement) {
            this.statement = statement;
        }

        public String getFinalBalance() {
            return finalBalance;
        }

        public void setFinalBalance(String finalBalance) {
            this.finalBalance = finalBalance;
        }

        public String getFinalBalanceType() {
            return finalBalanceType;
        }

        public void setFinalBalanceType(String finalBalanceType) {
            this.finalBalanceType = finalBalanceType;
        }

        public String getColorCode() {
            return colorCode;
        }

        public void setColorCode(String colorCode) {
            this.colorCode = colorCode;
        }

        public List<ProfileDetail> getProfileDetails() {
            return profileDetails;
        }

        public void setProfileDetails(List<ProfileDetail> profileDetails) {
            this.profileDetails = profileDetails;
        }

        public List<Invoice> getInvoices() {
            return invoices;
        }

        public void setInvoices(List<Invoice> invoices) {
            this.invoices = invoices;
        }

        public List<Payment> getPayments() {
            return payments;
        }

        public void setPayments(List<Payment> payments) {
            this.payments = payments;
        }

        public List<Quotation> getQuotations() {
            return quotations;
        }

        public void setQuotations(List<Quotation> quotations) {
            this.quotations = quotations;
        }

        public List<CreditNote> getCreditNote() {
            return creditNote;
        }

        public void setCreditNote(List<CreditNote> creditNote) {
            this.creditNote = creditNote;
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

    }

}
