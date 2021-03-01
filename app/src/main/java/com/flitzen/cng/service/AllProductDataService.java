package com.flitzen.cng.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flitzen.cng.CandG;
import com.flitzen.cng.Items.SaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.AllSaleData_Items;
import com.flitzen.cng.adapter.SaleSubCat_Items;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.WebApi;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;


public class AllProductDataService extends Service {

    private static String TAG = "AllProductDataService";
    private String userID = "";

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {

        private Message serviceID;
        Intent gcm_rec;

        public ServiceHandler(Looper looper) {
            super(looper);
            getAllProductData();
        }

        public void handleMessage(Message msg) {
            serviceID = msg;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void getAllProductData() {
            try {
                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<AllProductDataModel> call = webApi.allProductApi(getResources().getString(R.string.api_key));
                System.out.println("===========call    " + call.request().url());
                call.enqueue(new retrofit2.Callback<AllProductDataModel>() {
                    @Override
                    public void onResponse(Call<AllProductDataModel> call, retrofit2.Response<AllProductDataModel> response) {
                        stopSelf(serviceID.arg1);
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 1) {
                                CandG.arrayListAllData.clear();
                                for (int i = 0; i < response.body().getResult().size(); i++) {
                                    AllSaleData_Items item = new AllSaleData_Items();
                                    item.setcId(response.body().getResult().get(i).getCategoryId());
                                    item.setcName(response.body().getResult().get(i).getName());
                                    ArrayList<SaleSubCat_Items> arrayListSubCat = new ArrayList<>();
                                        for (int j=0;j<response.body().getResult().get(i).getSubcategory().size();j++){
                                            SaleSubCat_Items itemSub = new SaleSubCat_Items();
                                            itemSub.setcId(response.body().getResult().get(i).getSubcategory().get(j).getCategoryId());
                                            itemSub.setcName(response.body().getResult().get(i).getSubcategory().get(j).getName());
                                            ArrayList<SaleProducts_Items> arrayListProducts = new ArrayList<>();
                                            for(int k=0;k<response.body().getResult().get(i).getSubcategory().get(j).getProduct().size();k++){
                                                AllProductDataModel.Product product=response.body().getResult().get(i).getSubcategory().get(j).getProduct().get(k);
                                                SaleProducts_Items itemProduct = new SaleProducts_Items();
                                                itemProduct.setpId(product.getProductId());
                                                itemProduct.setpName(product.getName().toUpperCase());
                                                itemProduct.setpPrice(product.getPrice());
                                                itemProduct.setPzero_vat(product.getZeroVat());
                                                itemProduct.setCatName(response.body().getResult().get(i).getName());
                                                itemProduct.setSubcatName(response.body().getResult().get(i).getSubcategory().get(j).getName());
                                                arrayListProducts.add(itemProduct);
                                                CandG.arrayListProducts.add(itemProduct);
                                            }
                                            itemSub.setArrayListProducts(arrayListProducts);
                                            arrayListSubCat.add(itemSub);
                                        }
                                    item.setArrayListSubCat(arrayListSubCat);
                                    CandG.arrayListAllData.add(item);
                                }
                                gcm_rec = new Intent(getResources().getString(R.string.all_product_data));
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(gcm_rec);
                            } else {
                                new CToast(getApplicationContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                        .setBackgroundColor(R.color.msg_fail)
                                        .show();
                            }
                        } else {
                            new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AllProductDataModel> call, Throwable t) {
                        stopSelf(serviceID.arg1);
                        new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                });
            } catch (Exception e) {
                stopSelf(serviceID.arg1);
                new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();
            }
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("user_id")) {
                userID = intent.getStringExtra("user_id");
            }
        }
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);


        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
