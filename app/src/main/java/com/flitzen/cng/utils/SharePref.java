package com.flitzen.cng.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    public static String SharePrefName = "LOGIN_DETAILS";
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    public static String SYNC_STATUS = "SYNC_STATUS";
    public static String USERID = "USERID";
    public static String NAME = "NAME";
    public static String ORGID = "ORGID";
    public static String EMAIL = "EMAIL";
    public static String PASSWORD = "PASSWORD";
    public static String QT_MONTH = "QT_MONTH";
    public static String QT_YEAR = "QT_YEAR";

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(SharePrefName, Context.MODE_PRIVATE);
    }

    public static void Clear(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
