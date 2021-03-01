package com.flitzen.cng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.QuotationListAdapter;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuatationFragment extends Fragment {

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

    ArrayList<QuotationListingModel.Result> arrayList = new ArrayList<>();
    ArrayList<QuotationListingModel.Result> arrayListTemp = new ArrayList<>();
    public QuotationListAdapter mAdapter;
    int quotationListType;

    public QuatationFragment(int quotationListType) {
        this.quotationListType=quotationListType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewQuotation = inflater.inflate(R.layout.fragment_quatation, null);
        ButterKnife.bind(this, viewQuotation);
        if(Utils.isOnline(getActivity())){
            getQuotation();
        }else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
        performSomeOperations();
        return viewQuotation;
    }

    private void performSomeOperations() {

        recyclerview_quotation_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mAdapter = new QuotationListAdapter(getActivity(), arrayList);
        recyclerview_quotation_list.setAdapter(mAdapter);

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
                        if(arrayListTemp.get(i).getQuotationTo()!=null && arrayListTemp.get(i).getQuotationId()!=null && arrayListTemp.get(i).getTotalAmount()!=null
                                && arrayListTemp.get(i).getSalesPersonName()!=null){
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

    public void getQuotation(){
        new Thread(new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<QuotationListingModel> call = webApi.todayQuotationListApi(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<QuotationListingModel>() {
                    @Override
                    public void onResponse(Call<QuotationListingModel> call, retrofit2.Response<QuotationListingModel> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                viewContent.setVisibility(View.VISIBLE);
                                viewEmpty.setVisibility(View.GONE);
                                layout_empty.setVisibility(View.GONE);

                                txtTotalOrder.setText("Total Quotations : " + response.body().getTotal());

                                arrayList.clear();
                                arrayListTemp.clear();

                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    arrayList.add(response.body().getData().get(i));
                                    arrayListTemp.add(response.body().getData().get(i));
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
