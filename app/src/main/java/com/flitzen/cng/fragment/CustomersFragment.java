package com.flitzen.cng.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.activity.CustomerDetailsActivity;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.CustomerListAdapter;
import com.flitzen.cng.model.CustomerListModel;
import com.flitzen.cng.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.side_index)
    LinearLayout indexLayout;

    ArrayList<CustomerListModel.Result> customerList=new ArrayList<>();
    ArrayList<CustomerListModel.Result> customerListTemp=new ArrayList<>();
    CustomerListAdapter customerListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCustomers = inflater.inflate(R.layout.fragment_customers, container, false);
        ButterKnife.bind(this, viewCustomers);
        performSomeOperation();
        return viewCustomers;
    }

    private void performSomeOperation() {
        customerListAdapter = new CustomerListAdapter(customerList,getActivity());
        recyclerview_customer_list.setHasFixedSize(true);
        customerListAdapter.setClickListener(this);
        recyclerview_customer_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview_customer_list.setAdapter(customerListAdapter);


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
                        if (customerListTemp.get(i).getCompanyName().toLowerCase().contains(word)) {
                            customerList.add(customerListTemp.get(i));
                        } else if (customerListTemp.get(i).getPhoneNo().contains(word)) {
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
                if (b){
                    imgClearSearch.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);
                }
                else{
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
    }

    private void getIndexList(String[] fruits) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < fruits.length; i++) {
            String fruit = fruits[i];
            String index = fruit.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }

        fill++;
    }

    private void displayIndex() {

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());

        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }

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
        TextView tvTitle=((HomeActivity)getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.customers));
    }

    @Override
    public void onClickCustomer(View view, int position) {
        Utils.playClickSound(getActivity());
        Intent intent = new Intent(getActivity(), CustomerDetailsActivity.class);
       /* intent.putExtra("cust_id", customerList.get(position).getCustomerId());
        intent.putExtra("cust_name", customerList.get(position).getCompanyName());*/
        getActivity().startActivity(intent);
    }
}
