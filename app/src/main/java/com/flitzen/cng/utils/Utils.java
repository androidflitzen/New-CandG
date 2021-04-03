package com.flitzen.cng.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.flitzen.cng.R;

import java.io.File;

public class Utils {

    public static final String APP_DIRECTORY = ".CandG";
    public static boolean isConfigChange=false;
    public static MediaPlayer mMediaPlayer;
    public static MediaPlayer mMediaPlayer1;
    public static String getItemDir() {
        File dirReports = new File(Environment.getExternalStorageDirectory(),
               APP_DIRECTORY);
        if (!dirReports.exists()) {
            //dirReports.mkdirs();
            if (!dirReports.mkdirs()) {
                return null;
            }
        }
        return dirReports.getAbsolutePath();
    }

    public static void showLog(String msg){
        Log.v("CNG",msg);
    }

    public static void playClickSound(Context context) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context, R.raw.pop_click);
            mMediaPlayer.start();
        } else if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = MediaPlayer.create(context, R.raw.pop_click);
            mMediaPlayer.start();
        } else {
            mMediaPlayer.start();
        }
    }

    public static void playWelcomeSound(Context context) {
        if (mMediaPlayer1 == null) {
            mMediaPlayer1 = MediaPlayer.create(context, R.raw.welcome_msg);
            mMediaPlayer1.start();
        } else if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
            mMediaPlayer1.release();
            mMediaPlayer1 = MediaPlayer.create(context, R.raw.welcome_msg);
            mMediaPlayer1.start();
        } else {
            mMediaPlayer1.start();
        }
    }

    public static void updateApp(Context context) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.app_update_dialog, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);

        Button btnUpdate = (Button) promptsView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        alertDialog.show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
