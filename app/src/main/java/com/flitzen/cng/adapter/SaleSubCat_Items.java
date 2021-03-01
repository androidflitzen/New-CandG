package com.flitzen.cng.adapter;

import com.flitzen.cng.Items.SaleProducts_Items;

import java.util.ArrayList;

public class SaleSubCat_Items {
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

    public ArrayList<SaleProducts_Items> getArrayListProducts() {
        return arrayListProducts;
    }

    public void setArrayListProducts(ArrayList<SaleProducts_Items> arrayListProducts) {
        this.arrayListProducts = arrayListProducts;
    }

    ArrayList<SaleProducts_Items> arrayListProducts = new ArrayList<>();
    String cId;
    String cName;
}
