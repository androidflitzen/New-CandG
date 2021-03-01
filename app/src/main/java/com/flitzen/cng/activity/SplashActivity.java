package com.flitzen.cng.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.flitzen.cng.R;
import com.flitzen.cng.service.AllProductDataService;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;

import org.jsoup.Jsoup;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = SharePref.getSharePref(SplashActivity.this);
        if (sharedPreferences.getBoolean(SharePref.LOGIN_STATUS, false)) {
            Intent serviceIntent = new Intent(SplashActivity.this, AllProductDataService.class);
            serviceIntent.putExtra("user_id",sharedPreferences.getString(SharePref.USERID, ""));
            startService(serviceIntent);
        }

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 2000);
    }

    public void next() {
        if (Utils.isOnline(SplashActivity.this)) {
            /*VersionChecker versionChecker = new VersionChecker();
            String latestVersion = versionChecker.execute().get();
            if(latestVersion!=null){
                if(Double.parseDouble(latestVersion) == Double.parseDouble(getVersionCode(SplashActivity.this))){*/
            if (!sharedPreferences.getBoolean(SharePref.LOGIN_STATUS, false)) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                i.putExtra("type", "splash");
                startActivity(i);
            }
            finish();

                /*}else {
                    Utils.updateApp(SplashActivity.this);
                }
            }else {
                new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();
            }*/
        } else {
            new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

    }

    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";
    }

    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
    }
}