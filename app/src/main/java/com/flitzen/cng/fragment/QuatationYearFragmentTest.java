package com.flitzen.cng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.QuotationListAdapter;
import com.flitzen.cng.adapter.QuotationListAdapterTest;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class QuatationYearFragmentTest extends Fragment {

    View viewQuotation;

    @BindView(R.id.view_quotation_content)
    LinearLayout viewContent;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_clear_search)
    ImageView img_close;
    @BindView(R.id.txt_orders_count)
    TextView txtTotalOrder;
    @BindView(R.id.swipe_view_invoice_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_quatation_list)
    RecyclerView recyclerview_quotation_list;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;
    @BindView(R.id.view_quotation_empty)
    TextView viewEmpty;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;

    ArrayList<QuotationListingModel.Result> arrayList = new ArrayList<>();
    ArrayList<QuotationListingModel.Result> arrayListTemp = new ArrayList<>();
    ArrayList<Object> commonArrayList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    public QuotationListAdapterTest mAdapter;
    int quotationListType;
    int page = 1;
    int total_sale = 0;
    private boolean itShouldLoadMore = true;

    public QuatationYearFragmentTest(int quotationListType) {
        this.quotationListType = quotationListType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewQuotation = inflater.inflate(R.layout.fragment_quatation, null);
        ButterKnife.bind(this, viewQuotation);
        if (Utils.isOnline(getActivity())) {
            getQuotation(0);
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
                    getQuotation(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        performSomeOperations();
        return viewQuotation;
    }

    private void performSomeOperations() {

        recyclerview_quotation_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mAdapter = new QuotationListAdapterTest(getActivity(), commonArrayList);
        recyclerview_quotation_list.setAdapter(mAdapter);

        recyclerview_quotation_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            getQuotation(1);
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
                arrayList.addAll(arrayListTemp);
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
                if (word.trim().isEmpty()) {

                    img_close.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);

                    arrayList.addAll(arrayListTemp);
                    mAdapter.notifyDataSetChanged();
                } else {

                    img_close.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);

                    for (int i = 0; i < arrayListTemp.size(); i++) {
                        if (arrayListTemp.get(i).getQuotationTo() != null && arrayListTemp.get(i).getQuotationId() != null && arrayListTemp.get(i).getTotalAmount() != null
                                && arrayListTemp.get(i).getSalesPersonName() != null) {
                            if (arrayListTemp.get(i).getQuotationTo().toLowerCase().contains(word)) {
                                arrayList.add(arrayListTemp.get(i));
                            } else if (arrayListTemp.get(i).getQuotationId().toLowerCase().contains(word)) {
                                arrayList.add(arrayListTemp.get(i));
                            } else if (arrayListTemp.get(i).getTotalAmount().toLowerCase().contains(word)) {
                                arrayList.add(arrayListTemp.get(i));
                            } else if (arrayListTemp.get(i).getSalesPersonName().toLowerCase().contains(word)) {
                                arrayList.add(arrayListTemp.get(i));
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
    }

    public void getQuotation(int checkPagination) {

        if (checkPagination == 1) {
            itShouldLoadMore = false;
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }

        WebApi webApi = CandG.getClient().create(WebApi.class);
        Call<QuotationListingModel> call = webApi.yearQuotationListApi(getResources().getString(R.string.api_key), String.valueOf(page));
        call.enqueue(new Callback<QuotationListingModel>() {
            @Override
            public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                try {

                    if (checkPagination == 1) {
                        progressWheel.setVisibility(View.GONE);
                        itShouldLoadMore = true;
                    }

                    if (response.body().getStatus() == 1) {

                        viewContent.setVisibility(View.VISIBLE);
                        viewEmpty.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.GONE);

                        if (checkPagination == 0) {
                            txtTotalOrder.setText("Total Quotations : " + response.body().getTotal());
                            total_sale = Integer.parseInt(response.body().getTotal());

                            arrayList.clear();
                            arrayListTemp.clear();
                            commonArrayList.clear();
                            dateList.clear();
                        }

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            arrayList.add(response.body().getData().get(i));
                            arrayListTemp.add(response.body().getData().get(i));
                            if(dateList.contains(response.body().getData().get(i).getQuotationDate())){
                                commonArrayList.add(response.body().getData().get(i));
                            }else {
                                dateList.add(response.body().getData().get(i).getQuotationDate());
                                commonArrayList.add(response.body().getData().get(i).getQuotationDate());
                                commonArrayList.add(response.body().getData().get(i));
                            }
                        }

                        if (arrayList.size() < total_sale) {
                            page++;
                            itShouldLoadMore = true;
                        } else {
                            itShouldLoadMore = false;
                        }
                        mAdapter.notifyDataSetChanged();

                    } else {
                        viewContent.setVisibility(View.GONE);
                        viewEmpty.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QuotationListingModel> call, Throwable t) {
                if (checkPagination == 1) {
                    progressWheel.setVisibility(View.GONE);
                }
                itShouldLoadMore = true;
                swipeRefreshLayout.setRefreshing(false);
                new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();
            }
        });
    }
}
