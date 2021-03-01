package com.flitzen.cng.adapter;

import java.util.ArrayList;

public class AllSaleData_Items {
    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public ArrayList<SaleSubCat_Items> getArrayListSubCat() {
        return arrayListSubCat;
    }

    public void setArrayListSubCat(ArrayList<SaleSubCat_Items> arrayListSubCat) {
        this.arrayListSubCat = arrayListSubCat;
    }

    String cId;
    String cName;
    ArrayList<SaleSubCat_Items> arrayListSubCat = new ArrayList<>();
}
