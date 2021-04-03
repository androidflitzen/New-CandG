package com.flitzen.cng.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static boolean hasPermissions(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static void requestPermissions(Activity activity, String[] permissions, int REQUEST_CODE) {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        ActivityCompat.requestPermissions(activity, remainingPermissions.toArray(new String[remainingPermissions.size()]),REQUEST_CODE);
    }

}
