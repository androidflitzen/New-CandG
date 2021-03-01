package com.flitzen.cng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String AMOUNT_FORMATE = "##,##,###.##";
    public static String QTY_FORMATE = "##.##";

    public static String getCurrentDate(String formate) {
        return new SimpleDateFormat(formate).format(new Date());
    }

}
