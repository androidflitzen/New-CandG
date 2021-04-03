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
import com.flitzen.cng.adapter.CustomerCreditNoteListAdapter;
import com.flitzen.cng.model.CustomerCreditNoteListModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCreditNoteFragment extends Fragment {

    View viewCustomerCreditNote;
    private String customerId;
    private CustomerCreditNoteListAdapter customerCreditNoteListAdapter;
    private String TAG = "CustomerCreditNoteFragment";
    private boolean itShouldLoadMore = true;
    int page = 1;
    int total_sale = 0;
    ArrayList<CustomerCreditNoteListModel.Data> arrayList = new ArrayList<>();

    @BindView(R.id.recyclerview_creditNote_list_list)
    RecyclerView recyclerview_creditNote_list_list;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;
    @BindView(R.id.swipe_view_quotation_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.view_creditNote_content)
    RelativeLayout viewCreditNote;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;

    public CustomerCreditNoteFragment(String customerId) {
        this.customerId=customerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomerCreditNote = inflater.inflate(R.layout.fragment_customer_credit_note, null);
        ButterKnife.bind(this, viewCustomerCreditNote);

        recyclerview_creditNote_list_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview_creditNote_list_list.setHasFixedSize(true);
        customerCreditNoteListAdapter = new CustomerCreditNoteListAdapter(getActivity(), arrayList);
        recyclerview_creditNote_list_list.setAdapter(customerCreditNoteListAdapter);

        if (Utils.isOnline(getActivity())) {
            getCreditNoteList(0);
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
                    getCreditNoteList(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        recyclerview_creditNote_list_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            getCreditNoteList(1);
                        }
                    }
                }
            }
        });

        return viewCustomerCreditNote;
    }

    private void getCreditNoteList(int checkPagination) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerCreditNoteListModel> call = webApi.getCreditNoteList(getResources().getString(R.string.api_key), String.valueOf(page),customerId);
            call.enqueue(new Callback<CustomerCreditNoteListModel>() {
                @Override
                public void onResponse(Call<CustomerCreditNoteListModel> call, Response<CustomerCreditNoteListModel> response) {
                    swipeRefreshLayout.setRefreshing(false);

                    if (response.isSuccessful()){
                        if (checkPagination == 1) {
                            progressWheel.setVisibility(View.GONE);
                            itShouldLoadMore = true;
                        }

                        if (response.body().getStatus() == 1) {
                            viewCreditNote.setVisibility(View.VISIBLE);
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
                            customerCreditNoteListAdapter.updateList(arrayList);

                        } else {
                            page = 1;
                            arrayList.clear();
                            viewCreditNote.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerCreditNoteListModel> call, Throwable t) {
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
