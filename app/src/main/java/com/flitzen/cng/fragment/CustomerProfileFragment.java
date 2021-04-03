package com.flitzen.cng.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerDetailsModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerProfileFragment extends Fragment {

    View viewCustomerProfile;

    @BindView(R.id.txt_c_customer_name)
    TextView txt_c_name;
    @BindView(R.id.txt_c_phone_no)
    TextView txt_c_phone_no;
    @BindView(R.id.txt_c_email)
    TextView txt_c_email;
    @BindView(R.id.txt_c_address)
    TextView txt_c_address;
    @BindView(R.id.txt_c_post_code)
    TextView txt_c_post_code;
    @BindView(R.id.txt_c_city)
    TextView txt_c_city;
    @BindView(R.id.txt_c_credit_days)
    TextView txt_c_credit_days;
    @BindView(R.id.txt_c_credit_limit)
    TextView txt_c_credit_limit;
    @BindView(R.id.txt_c_closing_balance)
    TextView txt_c_closing_balance;

    String customerId;
    private String TAG = "CustomerProfileFragment";
    ProgressDialog prd;

    public CustomerProfileFragment(String customerId) {
        this.customerId=customerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerProfile = inflater.inflate(R.layout.fragment_customer_profile, null);
        ButterKnife.bind(this, viewCustomerProfile);

        if (Utils.isOnline(getActivity())) {
            getCustomerDetails();
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }


        return viewCustomerProfile;

    }

    private void getCustomerDetails() {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerDetailsModel> call = webApi.getCustomerDetails(getResources().getString(R.string.api_key),customerId);
            call.enqueue(new Callback<CustomerDetailsModel>() {
                @Override
                public void onResponse(Call<CustomerDetailsModel> call, Response<CustomerDetailsModel> response) {
                    hidePRD();
                    if(response.isSuccessful()){
                        if(response.body().getStatus()==1){
                           CustomerDetailsModel.Data data= response.body().getData().get(0);
                            txt_c_name.setText(data.getName());
                            txt_c_phone_no.setText(data.getPhoneNo());
                            txt_c_email.setText(data.getEmail());
                            txt_c_address.setText(data.getAddress());
                            txt_c_post_code.setText(data.getPostCode());
                            txt_c_city.setText(data.getCity());
                            txt_c_credit_days.setText(data.getCreditDays());
                            txt_c_credit_limit.setText(getResources().getString(R.string.pound) + " " + data.getCreditLimit());

                            if (data.getIsCrDr().equalsIgnoreCase("Cr")) {
                                txt_c_closing_balance.setText(Html.fromHtml(getResources().getString(R.string.pound) + " " + data.getClosingBalance() + " " + colorTextview(getResources().getColor(R.color.msg_success), "Cr")));
                            } else if ((data.getIsCrDr().equalsIgnoreCase("Dr"))) {
                                txt_c_closing_balance.setText(Html.fromHtml(getResources().getString(R.string.pound) + " " + data.getClosingBalance() + " " + colorTextview(getResources().getColor(R.color.colorPrimary), "Dr")));
                            } else {
                                txt_c_closing_balance.setText("-");
                            }
                        }else {
                            new CToast(getContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(getContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerDetailsModel> call, Throwable t) {
                    hidePRD();
                    new CToast(getContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        }catch (Exception e){
            hidePRD();
            Log.d(TAG, "Exception  profile Detail " + e.getMessage());
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }
    private String colorTextview(int color, String val2) {

        return "<font color=" + color + ">" + " " + val2 + "</font>";
    }

    public void showPRD() {

        if (prd == null) {
            prd = new ProgressDialog(getContext());
            prd.setMessage("Please wait...");
            prd.setCancelable(false);
            prd.show();
        }
    }

    public void hidePRD() {
        if (prd != null)
            prd.dismiss();
    }

}
