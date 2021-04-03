package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CustomerPaymentStatusAdapter;
import com.flitzen.cng.model.CustomerLedgerModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.swipe_view_ledger_list)
    SwipeRefreshLayout swipeViewLedgerList;
    @BindView(R.id.view_ledger_content)
    RelativeLayout viewLedgerContent;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;

    public CustomerPaymentStatusAdapter mAdapter;
    private String customerId;
    private String TAG = "CustomerPaymentStatusFragment";
    public ArrayList<CustomerLedgerModel.Data> arrayList = new ArrayList<>();

    public CustomerPaymentStatusFragment(String customerId) {
        this.customerId = customerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerPayment = inflater.inflate(R.layout.fragment_customer_payment_status, null);
        ButterKnife.bind(this, viewCustomerPayment);

        recyclerview_payment_status.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_payment_status.setHasFixedSize(true);
        mAdapter = new CustomerPaymentStatusAdapter(getActivity(), arrayList);
        recyclerview_payment_status.setAdapter(mAdapter);

        if (Utils.isOnline(getActivity())) {
            getLedger();
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        swipeViewLedgerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity())) {
                    getLedger();
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        return viewCustomerPayment;
    }

    private void getLedger() {
        try {
            swipeViewLedgerList.setRefreshing(true);
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerLedgerModel> call = webApi.getLedgerList(getResources().getString(R.string.api_key), customerId);
            call.enqueue(new Callback<CustomerLedgerModel>() {
                @Override
                public void onResponse(Call<CustomerLedgerModel> call, Response<CustomerLedgerModel> response) {
                    swipeViewLedgerList.setRefreshing(false);
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            viewLedgerContent.setVisibility(View.VISIBLE);
                            layout_empty.setVisibility(View.GONE);
                            arrayList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayList.add(response.body().getData().get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            viewLedgerContent.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        new CToast(getContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerLedgerModel> call, Throwable t) {
                    swipeViewLedgerList.setRefreshing(false);
                    new CToast(getContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            swipeViewLedgerList.setRefreshing(false);
            Log.d(TAG, "Exception Ledger List " + e.getMessage());
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }
}
