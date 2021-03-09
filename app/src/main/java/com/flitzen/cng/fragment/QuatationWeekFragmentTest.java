package com.flitzen.cng.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.QuotationListAdapter;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class QuatationWeekFragmentTest extends Fragment {

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
    ArrayList<QuotationListingModel.Result> arrayListSearch = new ArrayList<>();
    ArrayList<QuotationListingModel.Result> arrayListTemp = new ArrayList<>();
    public QuotationListAdapter mAdapter;
    int quotationListType;
    int page = 1;
    int pageForSearch = 1;
    int total_sale = 0;
    int total_sale_search = 0;
    private boolean itShouldLoadMore = true;
    private BroadcastReceiver mMyBroadcastReceiver;
    int whichAPICall = 0;

    public QuatationWeekFragmentTest(int quotationListType) {
        this.quotationListType=quotationListType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewQuotation = inflater.inflate(R.layout.fragment_quatation, null);
        ButterKnife.bind(this, viewQuotation);
        if(Utils.isOnline(getActivity())){
            getQuotation(0);
        }else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isOnline(getActivity())){
                    page = 1;
                    pageForSearch=1;
                    getQuotation(0);
                }else {
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
        mAdapter = new QuotationListAdapter(getActivity(), arrayList,1);
        recyclerview_quotation_list.setAdapter(mAdapter);

        recyclerview_quotation_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (whichAPICall == 0) {
                            if (itShouldLoadMore) {
                                getQuotation(1);
                            }
                        } else {
                            if (itShouldLoadMore) {
                                String charSequence = edtSearch.getText().toString().trim();
                                if (!charSequence.isEmpty()) {
                                    swipeRefreshLayout.setRefreshing(true);
                                    new searchData(charSequence, 1).execute();
                                }
                            }
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

                if (arrayList.size() > 0) {
                    viewContent.setVisibility(View.VISIBLE);
                    recyclerview_quotation_list.setVisibility(View.VISIBLE);
                    layout_empty.setVisibility(View.GONE);
                    viewEmpty.setVisibility(View.GONE);
                } else {
                    viewContent.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);
                    viewEmpty.setVisibility(View.VISIBLE);
                }

                txtTotalOrder.setText("Total Quotations : " + total_sale);
                //mAdapter.notifyDataSetChanged();
                mAdapter.updateList(arrayList);
                whichAPICall = 0;
                pageForSearch = 1;

                img_close.setVisibility(View.GONE);
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edtSearch.getText().toString().trim().isEmpty()) {
                        new CToast(getContext()).simpleToast("Please enter search text", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    } else {
                        performSearch();
                    }

                    return true;
                }
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                String word = edtSearch.getText().toString().trim().toLowerCase();

                if (word.trim().isEmpty()) {
                    arrayList.clear();
                    img_close.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);
                    arrayList.addAll(arrayListTemp);

                    if (arrayList.size() > 0) {
                        viewContent.setVisibility(View.VISIBLE);
                        recyclerview_quotation_list.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.GONE);
                        viewEmpty.setVisibility(View.GONE);
                    } else {
                        viewContent.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.VISIBLE);
                        viewEmpty.setVisibility(View.VISIBLE);
                    }

                    txtTotalOrder.setText("Total Quotations : " + total_sale);
                    whichAPICall = 0;
                    pageForSearch = 1;
                    mAdapter.updateList(arrayList);
                    //mAdapter.notifyDataSetChanged();
                } else {
                    img_close.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void performSearch() {
        pageForSearch = 1;
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        String charSequence = edtSearch.getText().toString().trim();
        if (!charSequence.isEmpty()) {
            swipeRefreshLayout.setRefreshing(true);
            new searchData(charSequence, 0).execute();
        }
    }

    public void getQuotation(int checkPagination){
        whichAPICall = 0;
        new Thread(new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(checkPagination==1){
                            itShouldLoadMore = false;
                            progressWheel.setVisibility(View.VISIBLE);
                        }else {
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    }
                });

                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<QuotationListingModel> call = webApi.weekQuotationListApi(getResources().getString(R.string.api_key),String.valueOf(page));
                call.enqueue(new Callback<QuotationListingModel>() {
                    @Override
                    public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                        try {

                            if(checkPagination==1){
                                progressWheel.setVisibility(View.GONE);
                                itShouldLoadMore = true;
                            }

                            if (response.body().getStatus() == 1) {

                                viewContent.setVisibility(View.VISIBLE);
                                viewEmpty.setVisibility(View.GONE);
                                layout_empty.setVisibility(View.GONE);

                                if(checkPagination==0){
                                    txtTotalOrder.setText("Total Quotations : " + response.body().getTotal());
                                    total_sale = Integer.parseInt(response.body().getTotal());

                                    arrayList.clear();
                                    arrayListTemp.clear();
                                }

                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    arrayList.add(response.body().getData().get(i));
                                    arrayListTemp.add(response.body().getData().get(i));
                                }

                                if (arrayList.size() < total_sale) {
                                    page++;
                                    itShouldLoadMore = true;
                                } else {
                                    itShouldLoadMore = false;
                                }
                                mAdapter.updateList(arrayList);

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
            }
        }).start();
    }

    class searchData extends AsyncTask<Void, Void, Void> {

        String searchWord;
        int checkPagination;

        public searchData(String s, int checkPagination) {
            searchWord = s.toLowerCase();
            this.checkPagination = checkPagination;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            whichAPICall = 1;
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<QuotationListingModel> call = webApi.weekQuotationListApi(getResources().getString(R.string.api_key), String.valueOf(pageForSearch), searchWord);
            call.enqueue(new Callback<QuotationListingModel>() {
                @Override
                public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        if (response.body().getStatus() == 1) {

                            viewContent.setVisibility(View.VISIBLE);
                            recyclerview_quotation_list.setVisibility(View.VISIBLE);
                            viewEmpty.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.GONE);

                            if (checkPagination == 0) {
                                txtTotalOrder.setText("Total Quotations : " + response.body().getTotal());
                                total_sale_search = Integer.parseInt(response.body().getTotal());

                                arrayListSearch.clear();
                            }

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayListSearch.add(response.body().getData().get(i));
                            }

                            if (arrayListSearch.size() < total_sale_search) {
                                pageForSearch++;
                                itShouldLoadMore = true;
                            } else {
                                itShouldLoadMore = false;
                            }
                            //mAdapter.notifyDataSetChanged();
                            mAdapter.updateList(arrayListSearch);

                        } else {
                            viewContent.setVisibility(View.VISIBLE);
                            recyclerview_quotation_list.setVisibility(View.GONE);
                            viewEmpty.setVisibility(View.VISIBLE);
                            layout_empty.setVisibility(View.VISIBLE);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mMyBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(getResources().getString(R.string.remove_week_quotation))) {
                    if (intent != null) {
                        if (whichAPICall == 0) {
                            total_sale = total_sale - 1;
                            txtTotalOrder.setText("Total Quotations : " + total_sale);
                        } else if (whichAPICall == 1) {
                            total_sale_search = total_sale_search - 1;
                            txtTotalOrder.setText("Total Quotations : " + total_sale_search);
                        }
                    }
                }
            }
        };
        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.remove_week_quotation)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
