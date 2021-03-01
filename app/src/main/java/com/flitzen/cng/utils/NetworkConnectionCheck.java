package com.flitzen.cng.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

public class NetworkConnectionCheck {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(final Context context) {
        int conn = NetworkConnectionCheck.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkConnectionCheck.TYPE_WIFI) {
            status = "Wifi enabled";
            // Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        } else if (conn == NetworkConnectionCheck.TYPE_MOBILE) {
            status = "Mobile data enabled";
            // Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        } else if (conn == NetworkConnectionCheck.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
            new AlertDialog.Builder(context)
                    .setMessage("Check your internet connection :(")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkConnectionCheck.getConnectivityStatusString(context);
                        }
                    })
                    .setNeutralButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .show();
            // Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        return status;
    }

}
