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
import com.flitzen.cng.adapter.CustomerInvoiceListAdapter;
import com.flitzen.cng.model.CustomerInvoiceListModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerInvoiceFragment extends Fragment {

    View viewCustomerInvoice;
    CustomerInvoiceListAdapter mAdapter;
    ArrayList<CustomerInvoiceListModel.Invoice> arrayList = new ArrayList<>();

    @BindView(R.id.linCustomerInvoice)
    LinearLayout linCustomerInvoice;
    @BindView(R.id.recyclerview_invoice_list)
    RecyclerView recyclerview_invoice_list;
    @BindView(R.id.textViewMsg)
    TextView txtViewMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerInvoice = inflater.inflate(R.layout.fragment_customer_invoice, null);
        ButterKnife.bind(this,viewCustomerInvoice);

        recyclerview_invoice_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_invoice_list.setHasFixedSize(true);
        mAdapter = new CustomerInvoiceListAdapter(getActivity(), arrayList);
        recyclerview_invoice_list.setAdapter(mAdapter);

        return viewCustomerInvoice;
    }
}
