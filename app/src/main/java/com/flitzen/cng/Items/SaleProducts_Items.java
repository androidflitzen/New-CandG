package com.flitzen.cng.Items;

public class SaleProducts_Items {

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

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

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpDetails() {
        return pDetails;
    }

    public void setpDetails(String pDetails) {
        this.pDetails = pDetails;
    }


    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

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

    public String getPzero_vat() {
        return pzero_vat;
    }

    public void setPzero_vat(String pzero_vat) {
        this.pzero_vat = pzero_vat;
    }

    String catName;
    String subcatName;
    String pId;
    String pName;
    String pPrice;
    String pImage;
    String pzero_vat;
    String pDetails;
    String pCatId;
    boolean isSelected = false;


}
