package com.flitzen.cng.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.CustomerDetailsActivity;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.CustomerListAdapter;
import com.flitzen.cng.model.CustomerListModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.test_customer_list.CityAdapterTest;
import com.flitzen.cng.test_customer_list.CustomerModelTest;
import com.flitzen.cng.test_customer_list.LetterComparator;
import com.flitzen.cng.test_customer_list.PinnedHeaderDecoration;
import com.flitzen.cng.test_customer_list.WaveSideBarView_A_Z;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomersFragment extends Fragment implements View.OnClickListener, CustomerListAdapter.ItemClickListener {

    View viewCustomers;
    Map<String, Integer> mapIndex;
    int fill = 0;

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_clear_search)
    ImageView imgClearSearch;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.recyclerview_customer_list)
    RecyclerView recyclerview_customer_list;
    @BindView(R.id.side_view)
    WaveSideBarView_A_Z side_view;

    ArrayList<CustomerModelTest> customerList = new ArrayList<>();
    ArrayList<CustomerModelTest> customerListTemp = new ArrayList<>();
    ArrayList<String> chare = new ArrayList<>();
    CustomerListAdapter customerListAdapter;
    private TextView txtAll;
    private ProgressDialog prd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomers = inflater.inflate(R.layout.fragment_customers, container, false);
        ButterKnife.bind(this, viewCustomers);

        prd = new ProgressDialog(getContext());
        prd.setMessage("Please wait...");
        prd.setCancelable(false);

        performSomeOperation();

        if (Utils.isOnline(getActivity())) {
            getCustomerList();
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
        return viewCustomers;
    }

    private void getCustomerList() {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CustomerModel> call = webApi.customerApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<CustomerModel>() {
                @Override
                public void onResponse(Call<CustomerModel> call, retrofit2.Response<CustomerModel> response) {
                    hidePRD();
                    if(response.isSuccessful()){
                        if(response.body().getStatus()==1){
                            String[] op_name_array = new String[response.body().getData().size()];
                            customerList.clear();
                            customerListTemp.clear();
                            chare.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                CustomerModel.Result customerModelTest = response.body().getData().get(i);
                                if (chare.contains(response.body().getData().get(i).getName().substring(0, 1))) {
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                    customerListTemp.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                } else {
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "1"));
                                    customerList.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                    customerListTemp.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "1"));
                                    customerListTemp.add(new CustomerModelTest(customerModelTest.getCustomerId(), customerModelTest.getName(), customerModelTest.getPhone_no(), "0"));
                                    chare.add(response.body().getData().get(i).getName().substring(0, 1));
                                }

                                op_name_array[i] = response.body().getData().get(i).getName().toUpperCase();
                            }
                            Collections.sort(customerList, new LetterComparator());
                            Collections.sort(customerListTemp, new LetterComparator());
                            customerListAdapter.notifyDataSetChanged();
                          /*  customerListAdapter = new CustomerListAdapter(customerList,getActivity());
                            recyclerview_customer_list.setAdapter(customerListAdapter);*/

                        }else {
                            new CToast(getContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(getContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerModel> call, Throwable t) {
                    hidePRD();
                    new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        }catch (Exception e){
            hidePRD();
            new CToast(getContext()).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    private void performSomeOperation() {
        customerListAdapter = new CustomerListAdapter(customerList, getActivity());
        recyclerview_customer_list.setHasFixedSize(true);
        customerListAdapter.setClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerview_customer_list.setAdapter(customerListAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (customerListAdapter.getItemViewType(position)) {
                    case 0:
                        return 1;
                    case 1:
                        return 3;
                    default:
                        return -1;
                }
            }
        });
        recyclerview_customer_list.setLayoutManager(gridLayoutManager);

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        recyclerview_customer_list.addItemDecoration(decoration);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                String word = edtSearch.getText().toString().trim().toLowerCase();
                customerList.clear();
                if (word.trim().isEmpty()) {
                    customerList.addAll(customerListTemp);
                    customerListAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < customerListTemp.size(); i++) {
                        if (customerListTemp.get(i).getName().toLowerCase().contains(word)) {
                            customerList.add(customerListTemp.get(i));
                        } else if (customerListTemp.get(i).getPhone_no().contains(word)) {
                            customerList.add(customerListTemp.get(i));
                        }
                    }
                    customerListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    imgClearSearch.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);
                } else {
                    imgClearSearch.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);
                }
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        imgClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                edtSearch.setText(null);
                edtSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        side_view.setOnTouchLetterChangeListener(new WaveSideBarView_A_Z.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = customerListAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    recyclerview_customer_list.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) recyclerview_customer_list.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }

    private void getIndexList(String[] latter) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < latter.length; i++) {
            String fruit = latter[i];
            String index = fruit.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }

        fill++;
    }

    @Override
    public void onClick(View v) {
        Utils.playClickSound(getActivity());
        TextView selectedIndex = (TextView) v;
        recyclerview_customer_list.scrollToPosition(mapIndex.get(selectedIndex.getText()));


        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_textview_alpha, null);
        dialog.setContentView(promptsView);

        final TextView alpha_txt = (TextView) promptsView.findViewById(R.id.text_alpha_dialog);

        alpha_txt.setText(selectedIndex.getText().toString().trim().toUpperCase());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 400);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tvTitle = ((HomeActivity) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.customers));
        txtAll = ((HomeActivity) getActivity()).findViewById(R.id.txtAll);
        txtAll.setVisibility(View.GONE);
    }

    @Override
    public void onClickCustomer(View view, int position) {
        Utils.playClickSound(getActivity());
        Intent intent = new Intent(getActivity(), CustomerDetailsActivity.class);
       /* intent.putExtra("cust_id", customerList.get(position).getCustomerId());
        intent.putExtra("cust_name", customerList.get(position).getCompanyName());*/
        getActivity().startActivity(intent);
    }

    public void showPRD() {
        if (prd != null && !prd.isShowing()) {
            prd.show();
        }
    }

    public void hidePRD() {
        if ((prd != null) && prd.isShowing()) {
            prd.dismiss();
        }
    }
}
