package com.flitzen.cng.fragment;

import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CreditNoteListAdapter;
import com.flitzen.cng.model.CrediNotesListModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class CreditNotesWeekFragmentTest extends Fragment {

    View viewCreditsNotes;

    @BindView(R.id.view_creditnote_content)
    LinearLayout viewInvoice;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_clear_search)
    ImageView img_clear_search;
    @BindView(R.id.swipe_view_creditnote_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_creditnote_list)
    RecyclerView recyclerview_crnote_list;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;
    @BindView(R.id.textViewMsg)
    TextView textViewMsg;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;
    @BindView(R.id.txt_orders_count)
    TextView txtTotalOrder;

    CreditNoteListAdapter creditNoteListAdapter;
    ArrayList<CrediNotesListModel.Result> arrayList = new ArrayList<>();
    ArrayList<CrediNotesListModel.Result> arrayListSearch = new ArrayList<>();
    ArrayList<CrediNotesListModel.Result> arrayListTemp = new ArrayList<>();
    private boolean itShouldLoadMore = true;
    int page = 1;
    int pageForSearch = 1;
    int total_sale = 0;
    int total_sale_search = 0;
    int whichAPICall = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCreditsNotes = inflater.inflate(R.layout.fragment_credit_notes, container, false);
        ButterKnife.bind(this, viewCreditsNotes);

        recyclerview_crnote_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        creditNoteListAdapter = new CreditNoteListAdapter(getActivity(), arrayListTemp);
        recyclerview_crnote_list.setAdapter(creditNoteListAdapter);

        if (Utils.isOnline(getActivity())) {
            getCreditNotes(0);
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
        performSomeOperation();
        return viewCreditsNotes;
    }

    private void getCreditNotes(int checkPagination) {
        whichAPICall = 0;
        new Thread(new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (checkPagination == 1) {
                            itShouldLoadMore = false;
                            progressWheel.setVisibility(View.VISIBLE);
                        } else {
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    }
                });

                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<CrediNotesListModel> call = webApi.weekCreditNoteList(getResources().getString(R.string.api_key), String.valueOf(page));
                call.enqueue(new Callback<CrediNotesListModel>() {
                    @Override
                    public void onResponse(Call<CrediNotesListModel> call, retrofit2.Response<CrediNotesListModel> response) {
                        if(response.isSuccessful()){
                            try {

                                if (checkPagination == 1) {
                                    progressWheel.setVisibility(View.GONE);
                                    itShouldLoadMore = true;
                                }

                                if (response.body().getStatus() == 1) {

                                    viewInvoice.setVisibility(View.VISIBLE);
                                    textViewMsg.setVisibility(View.GONE);
                                    layout_empty.setVisibility(View.GONE);

                                    if (checkPagination == 0) {
                                        txtTotalOrder.setText("Total Credit Notes : " + response.body().getTotal());
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
                                    creditNoteListAdapter.notifyDataSetChanged();

                                } else {
                                    viewInvoice.setVisibility(View.GONE);
                                    textViewMsg.setVisibility(View.VISIBLE);
                                    layout_empty.setVisibility(View.VISIBLE);
                                    textViewMsg.setText("No credit note has been created today");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<CrediNotesListModel> call, Throwable t) {
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
        }).start();

    }

    private void performSomeOperation() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity())) {
                    page = 1;
                    pageForSearch=1;
                    getCreditNotes(0);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

      /*  recyclerview_crnote_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        creditNoteListAdapter = new CreditNoteListAdapter(getActivity(), arrayListTemp);
        recyclerview_crnote_list.setAdapter(creditNoteListAdapter);*/

        recyclerview_crnote_list.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (whichAPICall == 0) {
                            if (itShouldLoadMore) {
                                getCreditNotes(1);
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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                String word = edtSearch.getText().toString().trim().toLowerCase();

                if (word.trim().isEmpty()) {
                    arrayList.clear();
                    img_clear_search.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);
                    arrayList.addAll(arrayListTemp);

                    if (arrayList.size() > 0) {
                        viewInvoice.setVisibility(View.VISIBLE);
                        recyclerview_crnote_list.setVisibility(View.VISIBLE);
                        layout_empty.setVisibility(View.GONE);
                        textViewMsg.setVisibility(View.GONE);
                    } else {
                        viewInvoice.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.VISIBLE);
                        textViewMsg.setVisibility(View.VISIBLE);
                    }

                    txtTotalOrder.setText("Total Quotations : " + total_sale);
                    whichAPICall = 0;
                    pageForSearch = 1;
                    creditNoteListAdapter.updateList(arrayList);
                    //mAdapter.notifyDataSetChanged();
                } else {
                    img_clear_search.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        img_clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                edtSearch.setText("");

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                arrayList.clear();
                arrayList.addAll(arrayListTemp);

                if (arrayList.size() > 0) {
                    viewInvoice.setVisibility(View.VISIBLE);
                    recyclerview_crnote_list.setVisibility(View.VISIBLE);
                    layout_empty.setVisibility(View.GONE);
                    textViewMsg.setVisibility(View.GONE);
                } else {
                    viewInvoice.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);
                    textViewMsg.setVisibility(View.VISIBLE);
                }

                txtTotalOrder.setText("Total Quotations : " + total_sale);
                //mAdapter.notifyDataSetChanged();
                creditNoteListAdapter.updateList(arrayList);
                whichAPICall = 0;
                pageForSearch = 1;

                img_clear_search.setVisibility(View.GONE);
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
            Call<CrediNotesListModel> call = webApi.weekCreditNoteList(getResources().getString(R.string.api_key), String.valueOf(pageForSearch), searchWord);
            call.enqueue(new Callback<CrediNotesListModel>() {
                @Override
                public void onResponse(Call<CrediNotesListModel> call, retrofit2.Response<CrediNotesListModel> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful()){
                        try {
                            if (response.body().getStatus() == 1) {

                                viewInvoice.setVisibility(View.VISIBLE);
                                recyclerview_crnote_list.setVisibility(View.VISIBLE);
                                textViewMsg.setVisibility(View.GONE);
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
                                creditNoteListAdapter.updateList(arrayListSearch);

                            } else {
                                viewInvoice.setVisibility(View.VISIBLE);
                                recyclerview_crnote_list.setVisibility(View.GONE);
                                textViewMsg.setVisibility(View.VISIBLE);
                                layout_empty.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        new CToast(getActivity()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<CrediNotesListModel> call, Throwable t) {
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
}

