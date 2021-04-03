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
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class CreditNotesFragmentToday extends Fragment {

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
    @BindView(R.id.txt_orders_count)
    TextView txtTotalOrder;

    CreditNoteListAdapter creditNoteListAdapter;
    ArrayList<CrediNotesListModel.Result> arrayList = new ArrayList<>();
    ArrayList<CrediNotesListModel.Result> arrayListTemp = new ArrayList<>();
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCreditsNotes = inflater.inflate(R.layout.fragment_credit_notes, container, false);
        ButterKnife.bind(this, viewCreditsNotes);

        if (Utils.isOnline(getActivity())) {
            getCreditNotes();
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
        performSomeOperation();
        return viewCreditsNotes;
    }

    private void performSomeOperation() {
        recyclerview_crnote_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        creditNoteListAdapter = new CreditNoteListAdapter(getActivity(), arrayListTemp);
        recyclerview_crnote_list.setAdapter(creditNoteListAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    arrayListTemp.clear();

                    img_clear_search.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);

                    String word = charSequence.toString().toLowerCase().trim();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getCreaditNoteId().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        } else if (arrayList.get(i).getSalesPersonName().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        } else if (arrayList.get(i).getCreditNoteTo().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                } else {
                    img_clear_search.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);

                    arrayListTemp.addAll(arrayList);
                }
                creditNoteListAdapter.notifyDataSetChanged();
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
                creditNoteListAdapter.notifyDataSetChanged();

                img_clear_search.setVisibility(View.GONE);
            }
        });
    }

    private void getCreditNotes() {
        new Thread(new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<CrediNotesListModel> call = webApi.todayCreditNoteList(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<CrediNotesListModel>() {
                    @Override
                    public void onResponse(Call<CrediNotesListModel> call, retrofit2.Response<CrediNotesListModel> response) {
                        if(response.isSuccessful()){
                            try {
                                if (response.body().getStatus() == 1) {
                                    viewInvoice.setVisibility(View.VISIBLE);
                                    textViewMsg.setVisibility(View.GONE);
                                    layout_empty.setVisibility(View.GONE);

                                    txtTotalOrder.setText("Total Credit Notes : " + response.body().getTotal());

                                    arrayListTemp.clear();
                                    arrayList.clear();

                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        arrayList.add(response.body().getData().get(i));
                                        arrayListTemp.add(response.body().getData().get(i));
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
                    }

                    @Override
                    public void onFailure(Call<CrediNotesListModel> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                });
            }
        }).start();
    }
}
