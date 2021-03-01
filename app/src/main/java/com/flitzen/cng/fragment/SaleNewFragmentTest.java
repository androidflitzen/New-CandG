/*
package com.flitzen.cng.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.flitzen.cng.Items.OnSaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.OnSaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleCategory_Adapter;
import com.flitzen.cng.adapter.SaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleSubCategory_Adapter;
import com.flitzen.cng.model.CategoryModel;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.model.SubCategoryModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.flitzen.cng.utils.WrapContentLinearLayoutManager;
import com.flitzen.cng.utils.WrapContentStaggedLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleNewFragmentTest extends Fragment implements View.OnClickListener {

    View viewSale;

    @BindView(R.id.edt_sale_search_products)
    EditText edtSearchProduct;
    @BindView(R.id.view_sale_discount)
    LinearLayout viewDiscount;
    @BindView(R.id.btn_sale_search_products)
    TextView btnSearchProduct;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.swipe_category)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_sales_category)
    RecyclerView recyclerviewCategory;
    @BindView(R.id.recyclerview_sales_sub_category)
    RecyclerView recyclerviewSubCategory;
    @BindView(R.id.recyclerview_sales_products)
    RecyclerView recyclerviewAllProducts;
    @BindView(R.id.fab_sales_add_product)
    FloatingActionButton fabAddProduct;
    @BindView(R.id.relNoItems)
    RelativeLayout relNoItems;
    @BindView(R.id.txt_sale_totalitems)
    TextView txtTotalItems;
    @BindView(R.id.recyclerview_sale_on_sale_products)
    RecyclerView recyclerviewOnSaleProducts;
    @BindView(R.id.view_sale_right_bottom)
    LinearLayout viewBottomRight;
    @BindView(R.id.txt_sale_totalprice)
    TextView txtTotalPrice;
    @BindView(R.id.txt_excluded_vat_amount)
    TextView txt_excluded_vat_amount;
    @BindView(R.id.txt_sale_tax_title)
    TextView txtTaxtitle;
    @BindView(R.id.txt_sale_tax_amount)
    TextView txtTaxAmount;
    @BindView(R.id.txt_sale_grand_total)
    TextView txtGrandTotal;
    @BindView(R.id.btn_sale_create_quotation)
    TextView btnQuotation;
    @BindView(R.id.btn_sale_create_invoice)
    TextView btnInvoice;
    @BindView(R.id.btn_sale_create_credit_note)
    TextView btnCreditNote;
    @BindView(R.id.txt_sale_top_sub_cat)
    TextView txtSubCatPath;
    @BindView(R.id.txt_sale_top_products)
    TextView txtProductPath;
    @BindView(R.id.txt_sale_discount_amount)
    TextView txtDiscountAmount;

    private MediaPlayer mediaPlayer, quotationPlayer, creditNotePlayer;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    private SaleCategory_Adapter mAdapterCategory;
    private SaleSubCategory_Adapter mAdapterSubCategory;
    private SaleProducts_Adapter mAdapterProducts;
    private OnSaleProducts_Adapter mAdapterOnSaleProducts;
    private ArrayList<OnSaleProducts_Items> arrayListOnSale = new ArrayList<>();
    private BroadcastReceiver mMyBroadcastReceiver;
    private SharedPreferences sharedPreferences;
    private ArrayList<CategoryModel.Result> categoryList = new ArrayList<>();
    private ArrayList<SubCategoryModel.Result> subCategoryList = new ArrayList<>();
    private ArrayList<ProductModel.Result> productList = new ArrayList<>();
    private ProgressDialog prd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewSale = inflater.inflate(R.layout.fragment_sale_new, container, false);
        ButterKnife.bind(this, viewSale);

        prd = new ProgressDialog(getContext());
        prd.setMessage("Please wait...");
        prd.setCancelable(false);
        sharedPreferences = SharePref.getSharePref(getActivity());

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.invoice_created_successfully);
        quotationPlayer = MediaPlayer.create(getActivity(), R.raw.quote_created_successfully);
        creditNotePlayer = MediaPlayer.create(getActivity(), R.raw.credit_note_created_successfully);

        recyclerviewCategory.setLayoutManager(new WrapContentStaggedLayoutManager(4, LinearLayoutManager.VERTICAL));
        recyclerviewSubCategory.setLayoutManager(new WrapContentStaggedLayoutManager(4, LinearLayoutManager.VERTICAL));
        recyclerviewAllProducts.setLayoutManager(new WrapContentStaggedLayoutManager(4, LinearLayoutManager.VERTICAL));

        fabAddProduct.setOnClickListener(this);

        mAdapterCategory = new SaleCategory_Adapter(getActivity(), categoryList);
        recyclerviewCategory.setAdapter(mAdapterCategory);

        mAdapterSubCategory = new SaleSubCategory_Adapter(getActivity(), subCategoryList);
        recyclerviewSubCategory.setAdapter(mAdapterSubCategory);

        mAdapterProducts = new SaleProducts_Adapter(getActivity(), productList);
        recyclerviewAllProducts.setAdapter(mAdapterProducts);

        recyclerviewOnSaleProducts.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        mAdapterOnSaleProducts = new OnSaleProducts_Adapter(getActivity(), arrayListOnSale);
        recyclerviewOnSaleProducts.setAdapter(mAdapterOnSaleProducts);

        if (Utils.isOnline(getActivity())) {
            getCategoryData();
        } else {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity())) {
                    getCategoryData();
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        mAdapterCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getActivity());
                if (Utils.isOnline(getActivity())) {
                    getSubCategoryData(position);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        mAdapterSubCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getActivity());
                if (Utils.isOnline(getActivity())) {
                    getProductData(position);
                } else {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }
        });

        mAdapterProducts.SetOnItemClickListner(new SaleProducts_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(final int position) {
                Utils.playClickSound(getActivity());
                OnSaleProducts_Items availableItem = null;
                for (int i = 0; i < arrayListOnSale.size(); i++) {
                    if (productList.get(position).getProductId().equals(arrayListOnSale.get(i).getpId())) {
                        availableItem = arrayListOnSale.get(i);
                    }
                }

                if (availableItem != null) {
                    availableItem.setpQty(availableItem.getpQty() + 1);
                } else {
                    OnSaleProducts_Items item = new OnSaleProducts_Items();
                    item.setpId(productList.get(position).getProductId());
                    item.setpName(productList.get(position).getName());
                    item.setpQty(1);
                    item.setZero_vat(productList.get(position).getZeroVat());
                    item.setpPrice(Double.parseDouble(productList.get(position).getPrice()));
                    item.setpOpValue(null);
                    item.setCatName(productList.get(position).getCategoryName());
                    item.setSubcatName(productList.get(position).getSubcategoryName());
                    arrayListOnSale.add(item);
                }
                mAdapterOnSaleProducts.notifyDataSetChanged();
                if (arrayListOnSale.size() > 0) {
                    relNoItems.setVisibility(View.GONE);
                    recyclerviewOnSaleProducts.setVisibility(View.VISIBLE);
                }
                setTotal();
            }
        });

        mAdapterOnSaleProducts.SetOnItemClickListner(new OnSaleProducts_Adapter.OnItemClickListner() {
            @Override
            public void updateTotal() {
                Utils.playClickSound(getActivity());
                setTotal();
            }
        });

        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSearchProduct.setTag("0");
                if (count <= 0) {
                    recyclerviewAllProducts.setVisibility(View.GONE);
                    recyclerviewCategory.setVisibility(View.VISIBLE);
                    txtSubCatPath.setVisibility(View.GONE);
                    txtProductPath.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        return viewSale;
    }

    private void getProductData(int position) {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<ProductModel> call = webApi.productApi(getResources().getString(R.string.api_key),subCategoryList.get(position).getCategoryId(),subCategoryList.get(position).getSubcategoryId());
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    hidePRD();
                    if(response.isSuccessful()){
                        if(response.body().getStatus()==1){
                            productList.clear();
                            for (int i=0;i<response.body().getData().size();i++){
                                productList.add(response.body().getData().get(i));
                            }
                            mAdapterProducts.notifyDataSetChanged();
                            recyclerviewSubCategory.setVisibility(View.GONE);
                            recyclerviewAllProducts.setVisibility(View.VISIBLE);
                            txtProductPath.setText(subCategoryList.get(position).getName());
                            txtProductPath.setVisibility(View.VISIBLE);
                        }else {
                            new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                    hidePRD();
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        }catch (Exception e){
            hidePRD();
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    private void getCategoryData() {
        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CategoryModel> call = webApi.categoryApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<CategoryModel>() {
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            categoryList.clear();
                            for(int i=0;i<response.body().getData().size();i++){
                                categoryList.add(response.body().getData().get(i));
                            }
                            stopAnim();
                            swipeRefreshLayout.setRefreshing(false);
                            mAdapterCategory.notifyDataSetChanged();
                        } else {
                            new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } else {
                        new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    private void getSubCategoryData(int position) {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<SubCategoryModel> call = webApi.subCategoryApi(getResources().getString(R.string.api_key), categoryList.get(position).getCategoryId());
            call.enqueue(new Callback<SubCategoryModel>() {
                @Override
                public void onResponse(Call<SubCategoryModel> call, Response<SubCategoryModel> response) {
                    hidePRD();
                    if (response.isSuccessful()){
                        if(response.body().getStatus()==1){
                            subCategoryList.clear();
                            for(int i=0;i<response.body().getData().size();i++){
                                subCategoryList.add(response.body().getData().get(i));
                            }
                            mAdapterSubCategory.notifyDataSetChanged();
                            recyclerviewSubCategory.setVisibility(View.VISIBLE);
                            recyclerviewCategory.setVisibility(View.GONE);
                            txtSubCatPath.setText(categoryList.get(position).getName());
                            txtSubCatPath.setVisibility(View.VISIBLE);
                        }else {
                            new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<SubCategoryModel> call, Throwable t) {
                    hidePRD();
                    new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            hidePRD();
            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tvTitle = ((HomeActivity) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.sale));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_sales_add_product:
                Utils.playClickSound(getActivity());
                openAddProduct();
                break;
        }
    }

    public void setTotal() {

        txtTotalItems.setText("(" + arrayListOnSale.size() + ")");
        if (arrayListOnSale.size() == 0) {
            viewBottomRight.setVisibility(View.GONE);
        } else {
            viewBottomRight.setVisibility(View.VISIBLE);

            double total = 0;
            double discountAmount = 0;
            double vatAmount = 0;
            double exclude_total = 0;
            double singleVatAmount = 0;
            double grandTotal = 0;

            for (int i = 0; i < arrayListOnSale.size(); i++) {

                double singleTotal = arrayListOnSale.get(i).getpPrice() * arrayListOnSale.get(i).getpQty();
                singleTotal = Double.parseDouble(formatter.format(singleTotal).replace(",", ""));
                Utils.showLog("=== singleTotal " + singleTotal + " price " + arrayListOnSale.get(i).getpPrice() + " qty " + arrayListOnSale.get(i).getpQty());

                double singleDiscount = (singleTotal * arrayListOnSale.get(i).getDiscount()) / 100;
                singleDiscount = Double.parseDouble(formatter.format(singleDiscount).replace(",", ""));

                Utils.showLog("=== singleDiscount " + singleDiscount);

                try {
                    if (arrayListOnSale.get(i).getZero_vat().equals("0")) {
                        singleVatAmount = ((singleTotal - singleDiscount) * 20) / 100;
                        Utils.showLog("=== singleVatAmount " + singleVatAmount);
                        vatAmount += singleVatAmount;
                    } else {
                        exclude_total += arrayListOnSale.get(i).getpPrice() * arrayListOnSale.get(i).getpQty();
                        Utils.showLog("=== exclude_total " + exclude_total);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                total += singleTotal;
                discountAmount += singleDiscount;

                grandTotal = total + vatAmount - discountAmount;

                Utils.showLog("=== net grandTotal " + grandTotal);
            }

            if (discountAmount > 0)
                viewDiscount.setVisibility(View.VISIBLE);
            else
                viewDiscount.setVisibility(View.GONE);

            double taxRate = Double.parseDouble("20");

            txtTotalPrice.setText(getResources().getString(R.string.pound) + " " + formatter.format(total - discountAmount));
            txtDiscountAmount.setText(getResources().getString(R.string.pound) + formatter.format(discountAmount));
            txtTaxAmount.setText(getResources().getString(R.string.pound) + formatter.format(vatAmount));

            txt_excluded_vat_amount.setText(getResources().getString(R.string.pound) + formatter.format(exclude_total));
            txtGrandTotal.setText(getResources().getString(R.string.pound) + formatter.format(grandTotal));

        }
    }

    public void openAddProduct() {

        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_add_product, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edt_product_name = (EditText) promptsView.findViewById(R.id.edt_product_name);
        final TextView txt_categoty = (TextView) promptsView.findViewById(R.id.txt_product_categoty);
        final EditText edt_base_price = (EditText) promptsView.findViewById(R.id.edt_base_price);
        final TextView txt_unit = (TextView) promptsView.findViewById(R.id.txt_unit);
        final ImageView img_close = (ImageView) promptsView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                alertDialog.dismiss();
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);

        Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        Button btn_add = (Button) promptsView.findViewById(R.id.btn_add);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    void stopAnim() {
        avi.setVisibility(View.GONE);
        avi.hide();
    }

    public void showPRD() {
        if (prd!=null && !prd.isShowing()){
            prd.show();
        }
    }

    public void hidePRD() {
        if ((prd != null) && prd.isShowing()){
            prd.dismiss();
        }
    }

    public void performSearch() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        String charSequence = edtSearchProduct.getText().toString().trim();
        if (!charSequence.isEmpty()) {
            recyclerviewAllProducts.setVisibility(View.VISIBLE);
           // new searchProducts(charSequence).execute();
            recyclerviewCategory.setVisibility(View.GONE);
            recyclerviewSubCategory.setVisibility(View.GONE);
            txtSubCatPath.setVisibility(View.GONE);
            txtProductPath.setVisibility(View.GONE);
        }
    }
}
*/
