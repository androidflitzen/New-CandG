package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CustomerPaymentStatusAdapter;
import com.flitzen.cng.model.CustomerPaymentModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerPaymentStatusFragment extends Fragment {

    View viewCustomerPayment;

    @BindView(R.id.textViewMsg)
    TextView txtViewMsg;
    @BindView(R.id.spn_month)
    TextView spn_month;
    @BindView(R.id.closing_bal)
    TextView closing_bal;
    @BindView(R.id.recyclerview_payment_list)
    RecyclerView recyclerview_payment_status;

    ArrayList<CustomerPaymentModel.Payment> arrayList = new ArrayList<>();
    public CustomerPaymentStatusAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerPayment = inflater.inflate(R.layout.fragment_customer_payment_status, null);
        ButterKnife.bind(this,viewCustomerPayment);

        recyclerview_payment_status.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_payment_status.setHasFixedSize(true);
        mAdapter = new CustomerPaymentStatusAdapter(getActivity(), arrayList);
        recyclerview_payment_status.setAdapter(mAdapter);

        return viewCustomerPayment;
    }
}
