package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flitzen.cng.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerProfile = inflater.inflate(R.layout.fragment_customer_profile, null);
        ButterKnife.bind(this, viewCustomerProfile);
        return viewCustomerProfile;

    }
}
