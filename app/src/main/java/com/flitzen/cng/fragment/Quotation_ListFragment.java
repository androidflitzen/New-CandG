package com.flitzen.cng.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.InvoicePagerAdapter;
import com.flitzen.cng.adapter.MonthListAdapter;
import com.flitzen.cng.adapter.QuotationListAdapter;
import com.flitzen.cng.adapter.YearListAdapter;
import com.flitzen.cng.model.MonthListModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.NDSpinner;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quotation_ListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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
    @BindView(R.id.relSelectMonth)
    RelativeLayout relSelectMonth;
    @BindView(R.id.cardSpinnerMonth)
    CardView cardSpinnerMonth;
    @BindView(R.id.txtSpinnerMonth)
    NDSpinner txtSpinnerMonth;
    @BindView(R.id.cardSpinnerYear)
    CardView cardSpinnerYear;
    @BindView(R.id.txtSpinnerYear)
    NDSpinner txtSpinnerYear;
    @BindView(R.id.txtMonthName)
    TextView txtMonthName;
    @BindView(R.id.txtYear)
    TextView txtYear;
    @BindView(R.id.relList)
    RelativeLayout relList;

    ArrayList<QuotationListingModel.Result> arrayList = new ArrayList<>();
    ArrayList<QuotationListingModel.Result> arrayListSearch = new ArrayList<>();
    ArrayList<QuotationListingModel.Result> arrayListTemp = new ArrayList<>();
    //  ArrayList<MonthListModel.Data> arrayListMonth = new ArrayList<>();
    List<String> arrayListMonth = new ArrayList<>();
    List<String> arrayListMonthNumber = new ArrayList<>();
    List<String> arrayListYear = new ArrayList<>();
    public QuotationListAdapter mAdapter;
    public ArrayAdapter<String> monthAdapter;
    public ArrayAdapter<String> yearAdapter;
    int quotationListType;
    int page = 1;
    int pageForSearch = 1;
    int total_sale = 0;
    int total_sale_search = 0;
    private boolean itShouldLoadMore = true;
    int whichAPICall = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String year, month;
    private String TAG = "Quotation_ListFragment";
    private TextView txtAll;
    private int clickState = 0;
    private boolean checkInitMonth = false, checkInitYear = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewQuotation = inflater.inflate(R.layout.fragment_quotation_list, container, false);
        ButterKnife.bind(this, viewQuotation);

        sharedPreferences = SharePref.getSharePref(getActivity());
        editor = sharedPreferences.edit();

        manageMonthAndYear();
        performSomeOperations();

        relSelectMonth.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity())) {
                    page = 1;
                    pageForSearch = 1;
                    getQuotation(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        return viewQuotation;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void performSomeOperations() {

        recyclerview_quotation_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mAdapter = new QuotationListAdapter(getActivity(), arrayList, 2);
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

                int tempTotal = 0;
                if (arrayList.size() > 0) {
                    relList.setVisibility(View.VISIBLE);
                    recyclerview_quotation_list.setVisibility(View.VISIBLE);
                    layout_empty.setVisibility(View.GONE);
                    viewEmpty.setVisibility(View.GONE);
                    tempTotal = total_sale;
                } else {
                    relList.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);
                    viewEmpty.setVisibility(View.VISIBLE);
                    tempTotal = 0;
                }

                txtTotalOrder.setText(String.valueOf(tempTotal));
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

                    int tempTotal = 0;
                    if (arrayList.size() > 0) {
                        relList.setVisibility(View.VISIBLE);
                        recyclerview_quotation_list.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.GONE);
                        viewEmpty.setVisibility(View.GONE);
                        tempTotal = total_sale;
                    } else {
                        relList.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.VISIBLE);
                        viewEmpty.setVisibility(View.VISIBLE);
                        tempTotal = 0;
                    }

                    txtTotalOrder.setText(String.valueOf(tempTotal));
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

        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.playClickSound(getActivity());
                return false;
            }
        });

    }

    private void manageMonthAndYear() {
        arrayListMonth = Arrays.asList(getResources().getStringArray(R.array.month_name));
        arrayListMonthNumber = Arrays.asList(getResources().getStringArray(R.array.month_position));
        // int monthIndex = LocalDate.now().getMonth().ordinal();//indexed from 0
        Calendar c = Calendar.getInstance();
        int next_month = c.get(Calendar.MONTH) + 1;
        Collections.rotate(arrayListMonth, -next_month);
        Collections.rotate(arrayListMonthNumber, -next_month);
        Collections.reverse(arrayListMonth);
        Collections.reverse(arrayListMonthNumber);
        // monthAdapter = new MonthListAdapter(getActivity(), R.layout.month_selection_layout, arrayListMonth);
        monthAdapter = new MonthListAdapter(getActivity(), arrayListMonth);
        txtSpinnerMonth.setOnItemSelectedListener(this);
        txtSpinnerMonth.setAdapter(monthAdapter);
        month = String.valueOf(next_month);

        arrayListYear = Arrays.asList(getResources().getStringArray(R.array.year));
        // monthAdapter = new MonthListAdapter(getActivity(), R.layout.month_selection_layout, arrayListMonth);
        yearAdapter = new YearListAdapter(getActivity(), arrayListYear);
        txtSpinnerYear.setOnItemSelectedListener(this);
        txtSpinnerYear.setAdapter(yearAdapter);
        year = arrayListYear.get(0);

        editor.putString(SharePref.QT_MONTH, String.valueOf(0));
        editor.putString(SharePref.QT_YEAR, String.valueOf(0));
        editor.commit();

        if (Utils.isOnline(getActivity())) {
            getQuotation(0);
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtAll:
                Utils.playClickSound(getActivity());
                clickState = 1;
                if (Utils.isOnline(getActivity())) {
                    getQuotation(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.txtSpinnerMonth:
                clickState = 0;
                page = 1;
                pageForSearch = 1;
                txtMonthName.setText(arrayListMonth.get(position));
                editor.putString(SharePref.QT_MONTH, String.valueOf(position));
                editor.commit();
                monthAdapter.notifyDataSetChanged();
                month = arrayListMonthNumber.get(position);

                if (checkInitMonth == false) {
                    checkInitMonth = true;
                } else {
                    if (Utils.isOnline(getActivity())) {
                        getQuotation(0);
                    } else {
                        new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }
                break;

            case R.id.txtSpinnerYear:
                clickState = 0;
                page = 1;
                pageForSearch = 1;
                txtYear.setText(arrayListYear.get(position));
                editor.putString(SharePref.QT_YEAR, String.valueOf(position));
                editor.commit();
                yearAdapter.notifyDataSetChanged();
                year = arrayListYear.get(position);

                if (checkInitYear == false) {
                    checkInitYear = true;
                }else {
                    if (Utils.isOnline(getActivity())) {
                        getQuotation(0);
                    } else {
                        new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("==========onNothingSelected");
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
            Call<QuotationListingModel> call = null;
            if (clickState == 0) {
                call = webApi.searchMonthYearQuotationListApi(getResources().getString(R.string.api_key), String.valueOf(pageForSearch), month, year, searchWord);
                System.out.println("===========call  " + call.request().url());
            } else {
                call = webApi.monthQuotationListApi(getResources().getString(R.string.api_key), String.valueOf(pageForSearch), searchWord);
            }
            call.enqueue(new Callback<QuotationListingModel>() {
                @Override
                public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getStatus() == 1) {

                                relList.setVisibility(View.VISIBLE);
                                recyclerview_quotation_list.setVisibility(View.VISIBLE);
                                viewEmpty.setVisibility(View.GONE);
                                layout_empty.setVisibility(View.GONE);

                                if (checkPagination == 0) {
                                    txtTotalOrder.setText(response.body().getTotal());
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
                                pageForSearch = 1;
                                arrayListSearch.clear();
                                relList.setVisibility(View.GONE);
                                // recyclerview_quotation_list.setVisibility(View.GONE);
                                viewEmpty.setVisibility(View.VISIBLE);
                                layout_empty.setVisibility(View.VISIBLE);
                                txtTotalOrder.setText("0");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
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

    public void getQuotation(int checkPagination) {
        whichAPICall = 0;
        if (checkPagination == 1) {
            itShouldLoadMore = false;
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }

        Log.e(TAG, "Month " + month);
        Log.e(TAG, "Year " + year);

        WebApi webApi = CandG.getClient().create(WebApi.class);
        Call<QuotationListingModel> call = null;
        if (clickState == 0) {
            call = webApi.monthYearQuotationListApi(getResources().getString(R.string.api_key), month, year, String.valueOf(page));
        } else if (clickState == 1) {
            call = webApi.yearQuotationListApi(getResources().getString(R.string.api_key), String.valueOf(page));
        }

        call.enqueue(new Callback<QuotationListingModel>() {
            @Override
            public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                if (response.isSuccessful()) {
                    try {

                        if (checkPagination == 1) {
                            progressWheel.setVisibility(View.GONE);
                            itShouldLoadMore = true;
                        }

                        if (response.body().getStatus() == 1) {

                            relList.setVisibility(View.VISIBLE);
                            viewEmpty.setVisibility(View.GONE);
                            layout_empty.setVisibility(View.GONE);

                            if (checkPagination == 0) {
                                txtTotalOrder.setText(response.body().getTotal());
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
                            // mAdapter.notifyDataSetChanged();

                        } else {
                            page = 1;
                            arrayList.clear();
                            arrayListTemp.clear();
                            relList.setVisibility(View.GONE);
                            viewEmpty.setVisibility(View.VISIBLE);
                            layout_empty.setVisibility(View.VISIBLE);
                            txtTotalOrder.setText("0");

                            //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
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


    @Override
    public void onResume() {
        super.onResume();
        TextView tvTitle = ((HomeActivity) getActivity()).findViewById(R.id.tvTitle);
        txtAll = ((HomeActivity) getActivity()).findViewById(R.id.txtAll);
        tvTitle.setText(getResources().getString(R.string.quotations));
        txtAll.setVisibility(View.VISIBLE);
        txtAll.setText(getResources().getString(R.string.all_quotations));
        txtAll.setOnClickListener(this);
    }
}
