package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CustomerMonthlyStatementAdapter;
import com.flitzen.cng.adapter.CustomerPaymentStatusAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerMontlyStatementFragment extends Fragment {

    View viewCustomerMonthly;

    @BindView(R.id.textViewMsg)
    TextView txtViewMsg;
    @BindView(R.id.ll_payment_status)
    LinearLayout ll_payment_status;
    @BindView(R.id.recyclerview_monthly_list)
    RecyclerView recyclerview_monthly_list;
    @BindView(R.id.txt_closing_balance)
    TextView txt_closing_balance;

   // ArrayList<Monthly_Statement_Items> arrayList = new ArrayList<>();
    public CustomerMonthlyStatementAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerMonthly = inflater.inflate(R.layout.fragment_customer_monthly_statement, null);
        ButterKnife.bind(this,viewCustomerMonthly);

        /*recyclerview_monthly_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_monthly_list.setHasFixedSize(true);
        mAdapter = new CustomerMonthlyStatementAdapter(getActivity(), arrayList);
        recyclerview_monthly_list.setAdapter(mAdapter);*/

        return viewCustomerMonthly;
    }
}
