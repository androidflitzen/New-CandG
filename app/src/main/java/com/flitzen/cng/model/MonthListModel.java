package com.flitzen.cng.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonthListModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("monthList")
    @Expose
    private List<Data> monthList = null;

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

    public List<Data> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<Data> monthList) {
        this.monthList = monthList;
    }

    public class Data{
        @SerializedName("monthName")
        @Expose
        private Integer monthName;
        @SerializedName("year")
        @Expose
        private String year;

        public Integer getMonthName() {
            return monthName;
        }

        public void setMonthName(Integer monthName) {
            this.monthName = monthName;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public Data(Integer monthName, String year) {
            this.monthName = monthName;
            this.year = year;
        }
    }

}
