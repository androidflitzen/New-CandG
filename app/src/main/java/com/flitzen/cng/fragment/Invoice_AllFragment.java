package com.flitzen.cng.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.InvoiceTodayListAdapter;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Invoice_AllFragment extends Fragment {

    View viewInvoiceWeek;

    @BindView(R.id.view_invoice_content)
    LinearLayout viewInvoice;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.txt_orders_count)
    TextView txtTotalOrder;
    @BindView(R.id.swipe_view_invoice_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_invoice_list)
    RecyclerView recyclerview_invoice_list;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;
    @BindView(R.id.textViewMsg)
    TextView textViewMsg;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;

    ArrayList<TodayInvoiceListingModel.Result> arrayList = new ArrayList<>();
    ArrayList<TodayInvoiceListingModel.Result> itemArrayTemp = new ArrayList<>();
    InvoiceTodayListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    private SharedPreferences sharedPreferences;
    int page = 1;
    private boolean itShouldLoadMore = true;
    int total_sale = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewInvoiceWeek = inflater.inflate(R.layout.fragment_invoice_today, container, false);
        ButterKnife.bind(this, viewInvoiceWeek);
        sharedPreferences = SharePref.getSharePref(getActivity());
        performSomeOperations();
        return viewInvoiceWeek;
    }

    private void performSomeOperations() {

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_invoice_list.setLayoutManager(mLayoutManager);
        mAdapter = new InvoiceTodayListAdapter(getActivity(), arrayList);
        recyclerview_invoice_list.setAdapter(mAdapter);

        if(Utils.isOnline(getActivity())){
            getInvoiceFirst(0);
        }else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        recyclerview_invoice_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            getInvoiceFirst(1);
                        }
                    }
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                edtSearch.setText("");

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                arrayList.clear();
                arrayList.addAll(itemArrayTemp);
                mAdapter.notifyDataSetChanged();

                img_close.setVisibility(View.GONE);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                String word = edtSearch.getText().toString().trim().toLowerCase();
                arrayList.clear();
                mAdapter.notifyDataSetChanged();

                if (word.trim().isEmpty()) {
                    img_close.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);

                    arrayList.addAll(itemArrayTemp);
                    mAdapter.notifyDataSetChanged();
                } else {
                    arrayList.clear();
                    mAdapter.notifyDataSetChanged();

                    img_close.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);

                    if (itemArrayTemp.size() > 0) {
                        for (int i = 0; i < itemArrayTemp.size(); i++) {
                            if (itemArrayTemp.get(i).getInvoiceTo().toLowerCase().contains(word)) {
                                arrayList.add(itemArrayTemp.get(i));
                            } else if (itemArrayTemp.get(i).getInvoiceId().toLowerCase().contains(word)) {
                                arrayList.add(itemArrayTemp.get(i));
                            } else if (itemArrayTemp.get(i).getTotalAmount().toLowerCase().contains(word)) {
                                arrayList.add(itemArrayTemp.get(i));
                            } else if (itemArrayTemp.get(i).getPaymentStatus().toLowerCase().contains(word)) {
                                arrayList.add(itemArrayTemp.get(i));
                            } else if (itemArrayTemp.get(i).getSalesPersonName().toLowerCase().contains(word)) {
                                arrayList.add(itemArrayTemp.get(i));
                            }
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isOnline(getActivity())){
                    page = 1;
                    getInvoiceFirst(0);
                }else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

    }

    public void getInvoiceFirst(int checkPagination){

        if(checkPagination==1){
            itShouldLoadMore = false;
            progressWheel.setVisibility(View.VISIBLE);
        }else {
            swipeRefreshLayout.setRefreshing(true);
        }

        swipeRefreshLayout.setRefreshing(true);
        if (!edtSearch.equals("")) {
            edtSearch.setText("");
        }

        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<TodayInvoiceListingModel> call = webApi.yearInvoiceList(getResources().getString(R.string.api_key),String.valueOf(page));
            call.enqueue(new Callback<TodayInvoiceListingModel>() {
                @Override
                public void onResponse(Call<TodayInvoiceListingModel> call, retrofit2.Response<TodayInvoiceListingModel> response) {
                    try {
                        if(response.isSuccessful()){

                            if(checkPagination==1){
                                progressWheel.setVisibility(View.GONE);
                                itShouldLoadMore = true;
                            }

                            if (response.body().getStatus() == 1) {

                                if(checkPagination==0){
                                    android.content.res.Resources res = getResources();
                                    String pound = res.getString(R.string.pound);
                                    txtTotalOrder.setText("Total Invoices : " + response.body().getTotal());
                                    total_sale = Integer.parseInt(response.body().getTotal());

                                    arrayList.clear();
                                    itemArrayTemp.clear();

                                }

                                textViewMsg.setVisibility(View.GONE);
                                layout_empty.setVisibility(View.GONE);
                                viewInvoice.setVisibility(View.VISIBLE);
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    arrayList.add(response.body().getData().get(i));
                                    itemArrayTemp.add(response.body().getData().get(i));
                                }

                                if (arrayList.size() < total_sale) {
                                    page++;
                                    itShouldLoadMore = true;
                                } else {
                                    itShouldLoadMore = false;
                                }

                                mAdapter.notifyDataSetChanged();

                            } else {
                                textViewMsg.setVisibility(View.VISIBLE);
                                layout_empty.setVisibility(View.VISIBLE);
                                viewInvoice.setVisibility(View.GONE);
                            }
                        }else {
                            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<TodayInvoiceListingModel> call, Throwable t) {
                    if(checkPagination==1){
                        progressWheel.setVisibility(View.GONE);
                    }
                    itShouldLoadMore = true;
                    swipeRefreshLayout.setRefreshing(false);
                    new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        }catch (Exception e){
            swipeRefreshLayout.setRefreshing(false);
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }
}
