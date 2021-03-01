package com.flitzen.cng;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.flitzen.cng.Items.SaleProducts_Items;
import com.flitzen.cng.adapter.AllSaleData_Items;
import com.flitzen.cng.model.AllProductDataModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CandG extends Application {

    public static String userId = "";
    public static String type = "";
    public static ArrayList<AllSaleData_Items> arrayListAllData = new ArrayList<>();
    public static ArrayList<SaleProducts_Items> arrayListProducts = new ArrayList<>();

    public static int Today_Page = 1;
    public static int Week_Page = 1;
    public static int Month_Page = 1;
    public static int All_Page = 1;

    public static final String BASE_URL = "https://newnew.candgbathrooms.com/apis/";
    private static Retrofit retrofit = null;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        CandG.context = getApplicationContext();
    }

    public static Retrofit getClient() {
        Retrofit retrofit = null;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
