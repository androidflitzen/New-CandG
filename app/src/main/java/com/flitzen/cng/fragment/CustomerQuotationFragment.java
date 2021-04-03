package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CustomerQuotationListAdapter;
import com.flitzen.cng.model.CustomerQuotationListModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerQuotationFragment extends Fragment {

    View viewCustomerQuotation;
    private String customerId;
    private CustomerQuotationListAdapter customerQuotationListAdapter;
    private String TAG = "CustomerQuotationFragment";
    private boolean itShouldLoadMore = true;
    int page = 1;
    int total_sale = 0;
    ArrayList<CustomerQuotationListModel.Data> arrayList = new ArrayList<>();

    @BindView(R.id.recyclerview_quotation_list)
    RecyclerView recyclerview_quotation_list;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;
    @BindView(R.id.swipe_view_quotation_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.view_quotation_content)
    RelativeLayout viewQuotation;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;

    public CustomerQuotationFragment(String customerId) {
        this.customerId=customerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerQuotation = inflater.inflate(R.layout.fragment_customer_quotation, null);
        ButterKnife.bind(this, viewCustomerQuotation);

        recyclerview_quotation_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_quotation_list.setHasFixedSize(true);
        customerQuotationListAdapter = new CustomerQuotationListAdapter(getActivity(), arrayList);
        recyclerview_quotation_list.setAdapter(customerQuotationListAdapter);

        if (Utils.isOnline(getActivity())) {
            getQuotationList(0);
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
                    getQuotationList(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        recyclerview_quotation_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            getQuotationList(1);
                        }
                    }
                }
            }
        });

        return viewCustomerQuotation;
    }

    private void getQuotationList(int checkPagination) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerQuotationListModel> call = webApi.getQuotationList(getResources().getString(R.string.api_key), String.valueOf(page),customerId);
            call.enqueue(new Callback<CustomerQuotationListModel>() {
                @Override
                public void onResponse(Call<CustomerQuotationListModel> call, Response<CustomerQuotationListModel> response) {
                    swipeRefreshLayout.setRefreshing(false);

                    if (response.isSuccessful()){
                        if (checkPagination == 1) {
                            progressWheel.setVisibility(View.GONE);
                            itShouldLoadMore = true;
                        }

                        if (response.body().getStatus() == 1) {
                            viewQuotation.setVisibility(View.VISIBLE);
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
                            customerQuotationListAdapter.updateList(arrayList);

                        } else {
                            page = 1;
                            arrayList.clear();
                            viewQuotation.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<CustomerQuotationListModel> call, Throwable t) {
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
