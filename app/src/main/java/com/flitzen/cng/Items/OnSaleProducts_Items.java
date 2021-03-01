package com.flitzen.cng.Items;

public class OnSaleProducts_Items {

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public double getpPrice() {
        return pPrice;
    }

    public void setpPrice(double pPrice) {
        this.pPrice = pPrice;
    }

    public double getpQty() {
        return pQty;
    }

    public void setpQty(double pQty) {
        this.pQty = pQty;
    }

    public String getpOpValue() {
        return pOpValue;
    }

    public void setpOpValue(String pOpValue) {
        this.pOpValue = pOpValue;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

   /* public SaleProducts_Items getProductItem() {
        return productItem;
    }

    public void setProductItem(SaleProducts_Items productItem) {
        this.productItem = productItem;
    }*/

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getZero_vat() {
        return zero_vat;
    }

    public void setZero_vat(String zero_vat) {
        this.zero_vat = zero_vat;
    }

    String catName;
    String subcatName;
   // SaleProducts_Items productItem;
    double vatAmount;
    double discount = 0;
    String pId;
    String zero_vat;
    String pName;
    double pPrice;
    double pQty;
    String pOpValue;
}
