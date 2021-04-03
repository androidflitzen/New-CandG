package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.flitzen.cng.adapter.CustomerInvoiceListAdapter;
import com.flitzen.cng.model.CustomerInvoiceListModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerInvoiceFragment extends Fragment {

    View viewCustomerInvoice;
    CustomerInvoiceListAdapter mAdapter;
    ArrayList<CustomerInvoiceListModel.Data> arrayList = new ArrayList<>();

    @BindView(R.id.linCustomerInvoice)
    LinearLayout linCustomerInvoice;
    @BindView(R.id.recyclerview_invoice_list)
    RecyclerView recyclerview_invoice_list;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;
    @BindView(R.id.view_invoice_empty)
    TextView viewEmpty;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;
    @BindView(R.id.view_invoice_content)
    RelativeLayout viewInvoice;
    @BindView(R.id.swipe_view_invoice_list)
    SwipeRefreshLayout swipeRefreshLayout;

    private String TAG = "CustomerInvoiceFragment";
    private boolean itShouldLoadMore = true;
    int page = 1;
    int total_sale = 0;
    private String customerId;

    public CustomerInvoiceFragment(String customerId) {
        this.customerId=customerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerInvoice = inflater.inflate(R.layout.fragment_customer_invoice, null);
        ButterKnife.bind(this, viewCustomerInvoice);

        recyclerview_invoice_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_invoice_list.setHasFixedSize(true);
        mAdapter = new CustomerInvoiceListAdapter(getActivity(), arrayList);
        recyclerview_invoice_list.setAdapter(mAdapter);

        if (Utils.isOnline(getActivity())) {
            getInvoiceList(0);
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity())) {
                    page = 1;
                    getInvoiceList(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        recyclerview_invoice_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            getInvoiceList(1);
                        }
                    }
                }
            }
        });

        return viewCustomerInvoice;
    }

    private void getInvoiceList(int checkPagination) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerInvoiceListModel> call = webApi.getInvoiceList(getResources().getString(R.string.api_key), String.valueOf(page),customerId);
            call.enqueue(new Callback<CustomerInvoiceListModel>() {
                @Override
                public void onResponse(Call<CustomerInvoiceListModel> call, Response<CustomerInvoiceListModel> response) {
                    swipeRefreshLayout.setRefreshing(false);

                    if(response.isSuccessful()){
                        if (checkPagination == 1) {
                            progressWheel.setVisibility(View.GONE);
                            itShouldLoadMore = true;
                        }

                        if (response.body().getStatus() == 1) {
                            viewInvoice.setVisibility(View.VISIBLE);
                            layout_empty.setVisibility(View.GONE);

                            if (checkPagination == 0) {
                                total_sale = Integer.parseInt(response.body().getTotal());

                                arrayList.clear();
                            }

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayList.add(response.body().getData().get(i));
                            }

                            if (arrayList.size() < total_sale) {
                                page++;
                                itShouldLoadMore = true;
                            } else {
                                itShouldLoadMore = false;
                            }
                            mAdapter.updateList(arrayList);

                        } else {
                            page = 1;
                            arrayList.clear();
                            viewInvoice.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.VISIBLE);
                            //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerInvoiceListModel> call, Throwable t) {
                    if (checkPagination == 1) {
                        progressWheel.setVisibility(View.GONE);
                    }
                    itShouldLoadMore = true;
                    swipeRefreshLayout.setRefreshing(false);
                    new CToast(getContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });

        } catch (Exception e) {
            swipeRefreshLayout.setRefreshing(false);
            Log.d(TAG, "Exception invoice list " + e.getMessage());
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }
}
