package com.flitzen.cng.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.CandG;
import com.flitzen.cng.Items.OnSaleProducts_Items;
import com.flitzen.cng.Items.SaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.OnSaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleCategory_Adapter;
import com.flitzen.cng.adapter.SaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleSubCat_Items;
import com.flitzen.cng.adapter.SaleSubCategory_Adapter;
import com.flitzen.cng.adapter.Spn_Adapter;
import com.flitzen.cng.model.AddQuotationModel;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.CategoryModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.model.ProductListRequestModel;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.model.SalesPersonModel;
import com.flitzen.cng.model.SubCategoryModel;

import com.flitzen.cng.service.AllProductDataService;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.PostPrams;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.flitzen.cng.utils.WrapContentLinearLayoutManager;
import com.flitzen.cng.utils.WrapContentStaggedLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleNewFragment extends Fragment implements View.OnClickListener {

    View viewSale;

    @BindView(R.id.edt_sale_search_products)
    EditText edtSearchProduct;
    @BindView(R.id.view_sale_discount)
    LinearLayout viewDiscount;
    /*  @BindView(R.id.btn_sale_search_products)
      TextView btnSearchProduct;*/
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
    @BindView(R.id.txt_sale_top_home)
    TextView txtHomePath;

    private MediaPlayer mediaPlayer, quotationPlayer, creditNotePlayer;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    private SaleCategory_Adapter mAdapterCategory;
    private SaleSubCategory_Adapter mAdapterSubCategory;
    private SaleProducts_Adapter mAdapterProducts;
    private OnSaleProducts_Adapter mAdapterOnSaleProducts;
    private ArrayList<OnSaleProducts_Items> arrayListOnSale = new ArrayList<>();
    private BroadcastReceiver mMyBroadcastReceiver;
    private SharedPreferences sharedPreferences;
    ArrayList<SaleSubCat_Items> arrayListSubCategory = new ArrayList<>();
    ArrayList<SaleProducts_Items> arrayListAllProductsTemp = new ArrayList<>();
    ArrayList<SaleProducts_Items> arrayListAllProducts = new ArrayList<>();
    ArrayList<CustomerModel.Result> arrayListCustomer = new ArrayList<>();
    ArrayList<SalesPersonModel.Result> arrayListSalesPerson = new ArrayList<>();
    private ProgressDialog prd;

    TextView txt_error_msg;
    TextView txt_credit_limit;
    TextView txt_credit_days;
    TextView txt_closing_balance;
    TextView tvTitle;
    View ll_not_eligible;

    View btnSave;
    View btnSavePrint;
    View btnBoth;
    View btnSaveDelivery;
    View btn_create_quotation;
    String saleType = "1";
    DecimalFormat formatterQty = new DecimalFormat(Helper.AMOUNT_FORMATE);
    private String TAG = "SaleNewFragment";
    boolean is_Eligible = true;
    String sale_type = "";
    String customer_name = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewSale = inflater.inflate(R.layout.fragment_sale_new, container, false);
        ButterKnife.bind(this, viewSale);

        getCustomer(0, 0);
        getSalesPersons(0, null);

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

        if (CandG.arrayListAllData.size() > 0) {
            stopAnim();
        }

        arrayListAllProducts.clear();
        arrayListAllProductsTemp.clear();
        arrayListAllProducts.addAll(CandG.arrayListProducts);
        arrayListAllProductsTemp.addAll(CandG.arrayListProducts);

        mAdapterCategory = new SaleCategory_Adapter(getActivity(), CandG.arrayListAllData);
        recyclerviewCategory.setAdapter(mAdapterCategory);

        mAdapterSubCategory = new SaleSubCategory_Adapter(getActivity(), arrayListSubCategory);
        recyclerviewSubCategory.setAdapter(mAdapterSubCategory);

        mAdapterProducts = new SaleProducts_Adapter(getActivity(), arrayListAllProductsTemp);
        recyclerviewAllProducts.setAdapter(mAdapterProducts);

        recyclerviewOnSaleProducts.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        mAdapterOnSaleProducts = new OnSaleProducts_Adapter(getActivity(), arrayListOnSale);
        recyclerviewOnSaleProducts.setAdapter(mAdapterOnSaleProducts);

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayListAllProducts.clear();
                CandG.arrayListAllData.clear();
                Intent serviceIntent = new Intent(getActivity(), AllProductDataService.class);
                serviceIntent.putExtra("user_id", sharedPreferences.getString(SharePref.USERID, ""));
                getActivity().startService(serviceIntent);
            }
        });

        mAdapterCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getActivity());
                arrayListSubCategory.clear();
                arrayListSubCategory.addAll(CandG.arrayListAllData.get(position).getArrayListSubCat());
                mAdapterSubCategory.notifyDataSetChanged();
                recyclerviewSubCategory.setVisibility(View.VISIBLE);
                recyclerviewCategory.setVisibility(View.GONE);
                txtSubCatPath.setText(CandG.arrayListAllData.get(position).getcName());
                txtSubCatPath.setVisibility(View.VISIBLE);
            }
        });

        mAdapterSubCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getActivity());
                arrayListAllProductsTemp.clear();
                arrayListAllProductsTemp.addAll(arrayListSubCategory.get(position).getArrayListProducts());
                mAdapterProducts.notifyDataSetChanged();

                recyclerviewSubCategory.setVisibility(View.GONE);
                recyclerviewAllProducts.setVisibility(View.VISIBLE);
                txtProductPath.setText(arrayListSubCategory.get(position).getcName());
                txtProductPath.setVisibility(View.VISIBLE);
            }
        });

        mAdapterProducts.SetOnItemClickListner(new SaleProducts_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(final int position) {
                Utils.playClickSound(getActivity());
                OnSaleProducts_Items availableItem = null;
                for (int i = 0; i < arrayListOnSale.size(); i++) {
                    if (arrayListAllProductsTemp.get(position).getpId().equals(arrayListOnSale.get(i).getpId())) {
                        availableItem = arrayListOnSale.get(i);
                    }
                }

                if (availableItem != null) {
                    availableItem.setpQty(availableItem.getpQty() + 1);
                } else {
                    OnSaleProducts_Items item = new OnSaleProducts_Items();
                    item.setpId(arrayListAllProductsTemp.get(position).getpId());
                    item.setpName(arrayListAllProductsTemp.get(position).getpName());
                    item.setpQty(1);
                    item.setZero_vat(arrayListAllProductsTemp.get(position).getPzero_vat());
                    item.setpPrice(Double.parseDouble(arrayListAllProductsTemp.get(position).getpPrice()));
                    item.setpOpValue(null);
                    item.setCatName(arrayListAllProductsTemp.get(position).getCatName());
                    item.setSubcatName(arrayListAllProductsTemp.get(position).getSubcatName());
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
                //  btnSearchProduct.setTag("0");
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

        txtHomePath.setOnClickListener(onTopPathItemClick);
        txtSubCatPath.setOnClickListener(onTopPathItemClick);
        txtProductPath.setOnClickListener(onTopPathItemClick);
        btnQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListCustomer.size() == 0) {
                    getCustomer(1, 1);
                } else {
                    createQuotationDialog(1);
                }
            }
        });
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (arrayListCustomer.size() == 0) {
                    getCustomer(1, 2);
                } else {
                    createInvoiceDialog();
                }
            }
        });

        btnCreditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (arrayListCustomer.size() == 0)
                    getCustomer(1, 3);
                else
                    createQuotationDialog(3);
            }
        });

        return viewSale;
    }

    private void getSalesPersons(int checkClick, TextView textView) {
        new Thread(new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (checkClick == 1) {
                            showPRD();
                        }
                    }
                });
                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<SalesPersonModel> call = webApi.salesPersonApi(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<SalesPersonModel>() {
                    @Override
                    public void onResponse(Call<SalesPersonModel> call, Response<SalesPersonModel> response) {
                        hidePRD();
                        if (response.isSuccessful()) {
                            arrayListSalesPerson.clear();
                            arrayListSalesPerson.addAll(response.body().getData());
                            if (checkClick == 1) {
                                selectSalesPerson(textView);
                            }
                        } else {
                            if (checkClick == 1) {
                                new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                        .setBackgroundColor(R.color.msg_fail)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SalesPersonModel> call, Throwable t) {
                        if (checkClick == 1) {
                            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                        hidePRD();
                    }
                });
            }
        }).start();
    }

    public void selectSalesPerson(final TextView txt) {
        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_spinner, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edtSearchUnit = (EditText) promptsView.findViewById(R.id.edt_spn_search);
        final ListView list_Unit = (ListView) promptsView.findViewById(R.id.list_spn);

        edtSearchUnit.setHint("Search Sales Person");

        final ArrayList<String> arrayListTemp = new ArrayList<>();
        for (int i = 0; i < arrayListSalesPerson.size(); i++) {
            arrayListTemp.add(arrayListSalesPerson.get(i).getName());
        }
        list_Unit.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));

        edtSearchUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {

                if (edtSearchUnit.getText().toString().trim().length() > 0) {
                    arrayListTemp.clear();
                    for (int j = 0; j < arrayListSalesPerson.size(); j++) {
                        String word = edtSearchUnit.getText().toString().toLowerCase();
                        if (arrayListSalesPerson.get(j).getName().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayListSalesPerson.get(j).getName());
                        }
                    }
                    list_Unit.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));
                } else {
                    arrayListTemp.clear();
                    for (int i = 0; i < arrayListSalesPerson.size(); i++) {
                        arrayListTemp.add(arrayListSalesPerson.get(i).getName());
                    }
                    list_Unit.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list_Unit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Utils.playClickSound(getActivity());
                for (int i = 0; i < arrayListSalesPerson.size(); i++) {
                    if (arrayListTemp.get(position).equals(arrayListSalesPerson.get(i).getName())) {
                        txt.setTag(arrayListSalesPerson.get(i).getSalesPersonId());
                        txt.setText(arrayListTemp.get(position));
                        alertDialog.dismiss();
                    }
                }
            }
        });

        alertDialog.show();
    }

    private void getCustomer(int checkClick, int btnType) {
        new Thread(new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (checkClick == 1) {
                            showPRD();
                        }
                    }
                });
                WebApi webApi = CandG.getClient().create(WebApi.class);
                Call<CustomerModel> call = webApi.customerApi(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<CustomerModel>() {
                    @Override
                    public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                        hidePRD();
                        if (response.isSuccessful()) {
                            arrayListCustomer.clear();
                            arrayListCustomer.addAll(response.body().getData());
                            if (btnType == 1) {
                                createQuotationDialog(btnType);
                            } else if (btnType == 2) {
                                createInvoiceDialog();
                            } else if (btnType == 3) {
                                createQuotationDialog(btnType);
                            }
                        } else {
                            if (checkClick == 1) {
                                new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                        .setBackgroundColor(R.color.msg_fail)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerModel> call, Throwable t) {
                        if (checkClick == 1) {
                            new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                        hidePRD();
                    }
                });
            }
        }).start();
    }

    public void createQuotationDialog(final int type) {

        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_create_quotation, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final View viewAccountSale = promptsView.findViewById(R.id.view_create_quo_account_sale);
        final View viewCashSale = promptsView.findViewById(R.id.view_create_quo_cash_sale);
        final ToggleSwitch toggleSwitch = promptsView.findViewById(R.id.toggle_create_quo);
        final TextInputEditText txtSelectCustomer = promptsView.findViewById(R.id.txt_create_quo_select_cutomer);
        final TextInputEditText edtCustomerName = promptsView.findViewById(R.id.edt_create_quo_select_cutomer);
        final EditText edtPo = promptsView.findViewById(R.id.edt_create_quo_po);
        TextView txtQuoTotalItems = promptsView.findViewById(R.id.txt_create_quo_total_items);
        TextView txtQuoTotalAmount = promptsView.findViewById(R.id.txt_create_quo_total_amount);
        View btnSave = promptsView.findViewById(R.id.btn_create_quo_save);
        View btnSavePrint = promptsView.findViewById(R.id.btn_create_quo_save_and_print);
        final TextInputEditText txtSelectSalesPerson = promptsView.findViewById(R.id.txt_create_quo_select_sales_person);
        TextView txt_msg = promptsView.findViewById(R.id.txt_msg);
        if (type == 3) {
            txt_msg.setText("Credit");
        }
        ImageView img_close = (ImageView) promptsView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        txtSelectSalesPerson.setTag(sharedPreferences.getString(SharePref.USERID, ""));
        txtSelectSalesPerson.setText(sharedPreferences.getString(SharePref.NAME, "").toUpperCase());
        txtSelectSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                if (arrayListSalesPerson.size() == 0) getSalesPersons(1, txtSelectSalesPerson);
                else selectSalesPerson(txtSelectSalesPerson);
            }
        });

        txtSelectCustomer.setTag("");
        txtQuoTotalItems.setText(txtTotalItems.getText());
        txtQuoTotalAmount.setText(txtGrandTotal.getText());

        txtSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                selectCustomer(txtSelectCustomer, false);
            }
        });


        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Utils.playClickSound(getActivity());
                if (position == 0) {
                    viewAccountSale.setVisibility(View.VISIBLE);
                    viewCashSale.setVisibility(View.GONE);
                    saleType = "1";
                } else {
                    viewCashSale.setVisibility(View.VISIBLE);
                    viewAccountSale.setVisibility(View.GONE);
                    saleType = "2";
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Select Customer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                alertDialog.dismiss();
                //TODO in future
                if (type == 3)
                    createCreditNote(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(),
                            edtCustomerName.getText().toString().trim(), false, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
                else
                    createQuatation(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(),
                            edtCustomerName.getText().toString().trim(), false, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
            }
        });

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Select Customer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                alertDialog.dismiss();

                //TODO in future
                if (type == 3)
                    createCreditNote(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(),
                            edtCustomerName.getText().toString().trim(), true, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
                else
                    createQuatation(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(),
                            edtCustomerName.getText().toString().trim(), true, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
            }
        });


        alertDialog.show();
    }

    String paymentType = "", deliveryType = "";
    double paidAMount = 0;

    public void selectCustomer(final TextView txt, final boolean isInvoice) {
        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_spinner, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edtSearchLocation = (EditText) promptsView.findViewById(R.id.edt_spn_search);
        final ListView list_location = (ListView) promptsView.findViewById(R.id.list_spn);

        final ArrayList<String> arrayListTemp = new ArrayList<>();
        for (int i = 0; i < arrayListCustomer.size(); i++) {
            arrayListTemp.add(arrayListCustomer.get(i).getName());
        }
        list_location.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));

        edtSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {

                if (edtSearchLocation.getText().toString().trim().length() > 0) {
                    arrayListTemp.clear();
                    for (int j = 0; j < arrayListCustomer.size(); j++) {
                        String word = edtSearchLocation.getText().toString().toLowerCase();
                        if (arrayListCustomer.get(j).getName().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayListCustomer.get(j).getName());
                        }
                    }
                    list_location.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));
                } else {
                    arrayListTemp.clear();
                    for (int i = 0; i < arrayListCustomer.size(); i++) {
                        arrayListTemp.add(arrayListCustomer.get(i).getName());
                    }
                    list_location.setAdapter(new Spn_Adapter(getActivity(), arrayListTemp));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Utils.playClickSound(getActivity());
                for (int i = 0; i < arrayListCustomer.size(); i++) {
                    if (arrayListCustomer.get(i).getName().toLowerCase().equals(arrayListTemp.get(position).toLowerCase())) {
                        String selectedCustomer = arrayListCustomer.get(i).getCustomerId();
                        if (arrayListTemp.get(position).equals("Add New Customer")) {
                            //TODO in future
                            //openAddCustomer(txt);
                        } else {
                            txt.setText(arrayListTemp.get(position));
                            txt.setTag(selectedCustomer);
                            if (isInvoice) {
                                //TODO in future
                                //checkEligiblityForInvoice(selectedCustomer, String.valueOf(paidAMount));
                            }
                        }
                    }
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void createInvoiceDialog() {
        paymentType = "";
        deliveryType = "";

        sale_type = "1";
        is_Eligible = true;

        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_create_invoice, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.show();

        btnSave = promptsView.findViewById(R.id.btn_create_inv_save);
        btnSavePrint = promptsView.findViewById(R.id.btn_create_inv_save_and_print);
        btnSaveDelivery = promptsView.findViewById(R.id.btn_create_inv_save_and_delivery_note);
        btnBoth = promptsView.findViewById(R.id.btn_create_inv_both);
        ImageView img_close = (ImageView) promptsView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        ll_not_eligible = promptsView.findViewById(R.id.ll_not_eligible);
        btn_create_quotation = promptsView.findViewById(R.id.btn_create_quotation);

        final View viewDelivery = promptsView.findViewById(R.id.view_create_inv_delivery);
        final View viewAccountSale = promptsView.findViewById(R.id.view_create_inv_account_sale);
        final View viewCashSale = promptsView.findViewById(R.id.view_create_inv_cash_sale);
        final ToggleSwitch toggleSwitch = promptsView.findViewById(R.id.toggle_create_inv);
        final TextInputEditText txtSelectCustomer = promptsView.findViewById(R.id.txt_create_inv_select_cutomer);
        final TextInputLayout txtSelectCustomerMain = promptsView.findViewById(R.id.txt_create_inv_select_cutomer_Main);
        final TextInputEditText edtCustomerName = promptsView.findViewById(R.id.edt_create_inv_select_cutomer);
        final TextInputLayout edtCustomerNameMain = promptsView.findViewById(R.id.edt_create_inv_select_cutomer_main);

        final TextInputEditText edtPo = promptsView.findViewById(R.id.edt_create_inv_po);
        final TextInputEditText edtAddress = promptsView.findViewById(R.id.edt_create_inv_address);
        edtAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final TextInputEditText edtPhone = promptsView.findViewById(R.id.edt_create_inv_phone);
        final TextInputEditText edtPINCode = promptsView.findViewById(R.id.edt_create_inv_pin_code);

        final EditText edtPaidAmount = promptsView.findViewById(R.id.edt_create_inv_paid_amount);
        final TextInputEditText edtNote = promptsView.findViewById(R.id.edt_create_inv_note);
        final RadioGroup rdbGroup = promptsView.findViewById(R.id.radio_grp_create_inv_delivery_type);
        final View viewReturnAmount = promptsView.findViewById(R.id.view_create_inv_return_amount);
        final TextView txtReturnAmount = promptsView.findViewById(R.id.txt_create_inv_return_amount);

        final RadioButton rdbCollection = promptsView.findViewById(R.id.rdb_create_inv_collection);
        final RadioButton rdbDelivery = promptsView.findViewById(R.id.rdb_create_inv_delivery);
        final RadioButton rdbCash = promptsView.findViewById(R.id.rdb_create_inv_cash);
        final RadioButton rdbCard = promptsView.findViewById(R.id.rdb_create_inv_card);
        final RadioButton rdbDebit = promptsView.findViewById(R.id.rdb_create_inv_debit);
        final RadioButton rdbOnline = promptsView.findViewById(R.id.rdb_create_inv_online);
        final RadioButton rdbCheque = promptsView.findViewById(R.id.rdb_create_inv_cheque);
        final RadioButton rdbOther = promptsView.findViewById(R.id.rdb_create_inv_other);

        final TextInputEditText txtSelectSalesPerson = promptsView.findViewById(R.id.txt_create_inv_select_sales_person);

        txt_error_msg = promptsView.findViewById(R.id.txt_error_msg);
        txt_credit_limit = promptsView.findViewById(R.id.txt_credit_limit);
        txt_credit_days = promptsView.findViewById(R.id.txt_credit_days);
        txt_closing_balance = promptsView.findViewById(R.id.txt_closing_balance);
        txtSelectSalesPerson.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        txtSelectSalesPerson.setTag(sharedPreferences.getString(SharePref.USERID, ""));
        txtSelectSalesPerson.setText(sharedPreferences.getString(SharePref.NAME, "").toUpperCase());
        txtSelectSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                if (arrayListSalesPerson.size() == 0) getSalesPersons(1, txtSelectSalesPerson);
                else selectSalesPerson(txtSelectSalesPerson);
            }
        });

        paidAMount = Double.parseDouble(txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
        edtPaidAmount.setText(txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));

        rdbCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b) {
                    btnSaveDelivery.setVisibility(View.GONE);
                    deliveryType = "1";
                }
            }
        });
        rdbDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b) {
                    if (is_Eligible) {
                        btnSaveDelivery.setVisibility(View.VISIBLE);
                        deliveryType = "2";
                    }
                }
            }
        });
        rdbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "0";
                //0= Cash, 1 = Card, 2 = Debit 3= Online, 4 = cheque, 5 =other
            }
        });

        rdbCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "1";
            }
        });
        rdbDebit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "2";
            }
        });
        rdbOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "3";
            }
        });
        rdbCheque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "4";
            }
        });
        rdbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getActivity());
                if (b)
                    paymentType = "5";
            }
        });

        edtPaidAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    double enteredAmount = Double.parseDouble(edtPaidAmount.getText().toString().trim());
                    double returnAmount = enteredAmount - paidAMount;
                    txtReturnAmount.setText(formatter.format(returnAmount));
                    if (returnAmount == 0) {
                        viewReturnAmount.setVisibility(View.GONE);
                    } else {
                        viewReturnAmount.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewReturnAmount.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Utils.playClickSound(getActivity());
                if (position == 0) {
                    viewAccountSale.setVisibility(View.VISIBLE);
                    txtSelectCustomer.setVisibility(View.VISIBLE);
                    txtSelectCustomerMain.setVisibility(View.VISIBLE);
                    viewCashSale.setVisibility(View.GONE);
                    edtCustomerName.setVisibility(View.GONE);
                    edtCustomerNameMain.setVisibility(View.GONE);
                    sale_type = "1";

                    if (!is_Eligible) {
                        ll_not_eligible.setVisibility(View.VISIBLE);

                        btnSave.setVisibility(View.GONE);
                        btnSavePrint.setVisibility(View.GONE);
                        btnSaveDelivery.setVisibility(View.GONE);
                        btnBoth.setVisibility(View.GONE);

                        btn_create_quotation.setVisibility(View.VISIBLE);

                    } else {

                        ll_not_eligible.setVisibility(View.GONE);

                        btnSave.setVisibility(View.VISIBLE);
                        btnSavePrint.setVisibility(View.VISIBLE);
                        btnSaveDelivery.setVisibility(View.GONE);
                        btnBoth.setVisibility(View.VISIBLE);

                        btn_create_quotation.setVisibility(View.GONE);
                    }
                } else {
                    edtCustomerName.setVisibility(View.VISIBLE);
                    edtCustomerNameMain.setVisibility(View.VISIBLE);
                    viewCashSale.setVisibility(View.VISIBLE);
                    //viewAccountSale.setVisibility(View.GONE);
                    txtSelectCustomer.setVisibility(View.GONE);
                    txtSelectCustomerMain.setVisibility(View.GONE);
                    sale_type = "2";

                    ll_not_eligible.setVisibility(View.GONE);

                    btnSave.setVisibility(View.VISIBLE);
                    btnSavePrint.setVisibility(View.VISIBLE);
                    btnSaveDelivery.setVisibility(View.GONE);
                    btnBoth.setVisibility(View.VISIBLE);

                    btn_create_quotation.setVisibility(View.GONE);

                }
            }
        });

        txtSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                selectCustomer(txtSelectCustomer, true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(getContext()).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        customer_name = txtSelectCustomer.getText().toString();
                        params.put("customer_id", txtSelectCustomer.getTag().toString());
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                        alertDialog.dismiss();
                        createInvoice(params, false, false, false);
                    }

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        //if (edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {
                        Map<String, String> params = new HashMap<>();
                        customer_name = edtCustomerName.getText().toString().trim();
                        params.put("customer_name", edtCustomerName.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                        params.put("payment_mode", paymentType);
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("paid", edtPaidAmount.getText().toString().trim());
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                        alertDialog.dismiss();
                        createInvoice(params, false, false, false);
                    }
                }
            }
        });

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(getContext()).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        customer_name = txtSelectCustomer.getText().toString();
                        params.put("customer_id", txtSelectCustomer.getTag().toString());
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());
                        alertDialog.dismiss();
                        createInvoice(params, true, false, false);
                    }

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        customer_name = edtCustomerName.getText().toString().trim();
                        params.put("customer_name", edtCustomerName.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                        params.put("payment_mode", paymentType);
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("paid", edtPaidAmount.getText().toString().trim());
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());
                        alertDialog.dismiss();
                        createInvoice(params, true, false, false);
                    }
                }
            }
        });


        btnSaveDelivery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(getContext()).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", txtSelectCustomer.getTag().toString());
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());
                        alertDialog.dismiss();
                        createInvoice(params, false, true, false);

                    }
                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_name", edtCustomerName.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                        params.put("payment_mode", paymentType);
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("paid", edtPaidAmount.getText().toString().trim());
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());
                        alertDialog.dismiss();
                        createInvoice(params, false, true, false);
                    }
                }
            }
        });

        btnBoth.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(getContext()).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        //if (edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", txtSelectCustomer.getTag().toString());
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                        alertDialog.dismiss();
                        createInvoice(params, false, false, true);
                    }
                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    } else if (deliveryType == null || deliveryType.isEmpty() || deliveryType.equals("null")) {
                        Toast.makeText(getActivity(), "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (deliveryType.equals("2") && edtAddress.getText().toString().trim().isEmpty()) {
                        edtAddress.setError("Enter Delivery Address");
                        edtAddress.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        params.put("customer_name", edtCustomerName.getText().toString().trim());
                        params.put("sales_type", sale_type);
                        params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                        params.put("payment_mode", paymentType);
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_pincode", edtPINCode.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("payment_amount", edtPaidAmount.getText().toString().trim());
                        params.put("invoice_notes", edtNote.getText().toString().trim());
                        params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                        alertDialog.dismiss();
                        createInvoice(params, false, false, true);
                    }
                }
            }
        });

        btn_create_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                createQuatation(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(), edtCustomerName.getText().toString().trim(), false
                        , txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
                alertDialog.dismiss();
            }
        });
    }


    View.OnClickListener onTopPathItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.playClickSound(getActivity());
            if (v.getTag().toString().equals("0")) {
                recyclerviewCategory.setVisibility(View.VISIBLE);
                recyclerviewSubCategory.setVisibility(View.GONE);
                recyclerviewAllProducts.setVisibility(View.GONE);
                txtSubCatPath.setVisibility(View.GONE);
                txtProductPath.setVisibility(View.GONE);
            } else if (v.getTag().toString().equals("1")) {
                recyclerviewSubCategory.setVisibility(View.VISIBLE);
                recyclerviewAllProducts.setVisibility(View.GONE);
                txtProductPath.setVisibility(View.GONE);
            }
        }
    };

    public void createInvoice(Map<String, String> params, final boolean isPrintReceipt,
                              final boolean isDeliveryNote, final boolean isPrintBoth) {
        SharedPreferences sharedPreferences = SharePref.getSharePref(getActivity());
        showPRD();
        params.put("api_key", getResources().getString(R.string.api_key));
        params.put("user_id", sharedPreferences.getString(SharePref.USERID, ""));
        params.put("total_amount", txtTotalPrice.getText().toString().trim().substring(1, txtTotalPrice.getText().toString().trim().length()).replace(",", ""));
        params.put("final_total", txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
        params.put("vat_amount", txtTaxAmount.getText().toString().trim().substring(1, txtTaxAmount.getText().toString().trim().length()).replace(",", ""));
        params.put("discount_amount", txtDiscountAmount.getText().toString().trim().substring(1, txtDiscountAmount.getText().toString().trim().length()).replace(",", ""));
        params.put("zero_vat_total", txt_excluded_vat_amount.getText().toString().trim().substring(1, txt_excluded_vat_amount.getText().toString().trim().length()));

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrayListOnSale.size(); i++) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("product_id", arrayListOnSale.get(i).getpId());
                jsonObject.put("product_price", String.valueOf(arrayListOnSale.get(i).getpPrice()));
                jsonObject.put("product_qty", formatterQty.format(arrayListOnSale.get(i).getpQty()));
                jsonObject.put("product_discount_percentage", String.valueOf(arrayListOnSale.get(i).getDiscount()));
                Double discount = (arrayListOnSale.get(i).getpQty() * arrayListOnSale.get(i).getpPrice()) / 100 * arrayListOnSale.get(i).getDiscount();
                discount = Double.parseDouble(formatter.format(discount).replace(",", ""));
                Double total = (arrayListOnSale.get(i).getpQty() * Double.valueOf(arrayListOnSale.get(i).getpPrice())) - discount;
                jsonObject.put("product_final_price", formatter.format(total).replace(",", ""));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        params.put("invoice_data", jsonArray.toString());

        WebApi webApi = CandG.getClient().create(WebApi.class);
        Log.d(TAG, "Add Invoice param " + params.toString());
        Call<AddQuotationModel> call = webApi.addInvoiceApi(params);
        Log.d(TAG, "Add Invoice API  " + call.request().toString());
        call.enqueue(new Callback<AddQuotationModel>() {
            @Override
            public void onResponse(Call<AddQuotationModel> call, Response<AddQuotationModel> response) {
                hidePRD();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        mediaPlayer.start();
                        new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_success)
                                .show();

                        arrayListOnSale.clear();
                        mAdapterOnSaleProducts.notifyDataSetChanged();
                        setTotal();

                        //TODO PDF link generation api
                        if (isPrintReceipt) {

                        }
                        //TODO PDF link generation api
                        else if (isDeliveryNote) {

                        }
                        //TODO PDF link generation api
                        else if (isPrintBoth) {

                        }

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
            public void onFailure(Call<AddQuotationModel> call, Throwable t) {
                hidePRD();
                new CToast(getActivity()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();

            }
        });
    }

    public void createQuatation(final int type, final String customerId, final String customerName,
                                final boolean isPrintReceipt, final String salesPerson, final String PO) {
        try {
            showPRD();
            Map<String, String> params = new HashMap<>();
            params.put("api_key", getResources().getString(R.string.api_key));
            params.put("user_id", sharedPreferences.getString(SharePref.USERID, ""));
            params.put("customer_id", customerId);
            params.put("customer_name", customerName);
            params.put("sales_person_id", salesPerson);
            params.put("total_amount", txtTotalPrice.getText().toString().trim().substring(1, txtTotalPrice.getText().toString().trim().length()).replace(",", ""));
            params.put("vat_amount", txtTaxAmount.getText().toString().trim().substring(1, txtTaxAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("final_total", txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
            params.put("sales_type", saleType);
            params.put("purchase_no", PO);
            params.put("discount_amount", txtDiscountAmount.getText().toString().trim().substring(1, txtDiscountAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("zero_vat_total", txt_excluded_vat_amount.getText().toString().trim().substring(1, txt_excluded_vat_amount.getText().toString().trim().length()));
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < arrayListOnSale.size(); i++) {

                Double discount = (arrayListOnSale.get(i).getpQty() * arrayListOnSale.get(i).getpPrice()) / 100 * arrayListOnSale.get(i).getDiscount();
                discount = Double.parseDouble(formatter.format(discount).replace(",", ""));
                Double total = (arrayListOnSale.get(i).getpQty() * Double.valueOf(arrayListOnSale.get(i).getpPrice())) - discount;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("product_id", arrayListOnSale.get(i).getpId());
                    jsonObject.put("product_price", String.valueOf(arrayListOnSale.get(i).getpPrice()));
                    jsonObject.put("product_qty", formatterQty.format(arrayListOnSale.get(i).getpQty()));
                    jsonObject.put("product_discount_percentage", String.valueOf(arrayListOnSale.get(i).getDiscount()));
                    jsonObject.put("product_final_price", String.valueOf(total).replace(",", ""));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            params.put("quotation_data", jsonArray.toString());
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Log.d(TAG, "Add Quotation param " + params.toString());
            Call<AddQuotationModel> call = webApi.addQuotationApi(params);
            Log.d(TAG, "Add Quotation API  " + call.request().toString());
            call.enqueue(new Callback<AddQuotationModel>() {
                @Override
                public void onResponse(Call<AddQuotationModel> call, Response<AddQuotationModel> response) {
                    hidePRD();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();

                            quotationPlayer.start();
                            arrayListOnSale.clear();
                            mAdapterOnSaleProducts.notifyDataSetChanged();
                            setTotal();

                            //TODO PDF link generation api
                            if (isPrintReceipt) {

                            }

                            txtHomePath.setVisibility(View.VISIBLE);
                            txtSubCatPath.setVisibility(View.GONE);
                            txtProductPath.setVisibility(View.GONE);
                            recyclerviewAllProducts.setVisibility(View.GONE);
                            recyclerviewSubCategory.setVisibility(View.GONE);
                            recyclerviewCategory.setVisibility(View.VISIBLE);

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
                public void onFailure(Call<AddQuotationModel> call, Throwable t) {
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

    public void createCreditNote(final int type, final String customerId, final String customerName,
                                 final boolean isPrintReceipt, final String salesPerson, final String PO) {

        try {
            showPRD();
            Map<String, String> params = new HashMap<>();
            params.put("api_key", getResources().getString(R.string.api_key));
            params.put("user_id", sharedPreferences.getString(SharePref.USERID, ""));
            params.put("customer_id", customerId);
            params.put("customer_name", customerName);
            params.put("sales_person_id", salesPerson);
            params.put("total_amount", txtTotalPrice.getText().toString().trim().substring(1, txtTotalPrice.getText().toString().trim().length()).replace(",", ""));
            params.put("vat_amount", txtTaxAmount.getText().toString().trim().substring(1, txtTaxAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("final_total", txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
            params.put("sales_type", saleType);
            params.put("purchase_no", PO);
            params.put("discount_amount", txtDiscountAmount.getText().toString().trim().substring(1, txtDiscountAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("zero_vat_total", txt_excluded_vat_amount.getText().toString().trim().substring(1, txt_excluded_vat_amount.getText().toString().trim().length()));

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < arrayListOnSale.size(); i++) {

                Double discount = (arrayListOnSale.get(i).getpQty() * arrayListOnSale.get(i).getpPrice()) / 100 * arrayListOnSale.get(i).getDiscount();
                discount = Double.parseDouble(formatter.format(discount).replace(",", ""));
                Double total = (arrayListOnSale.get(i).getpQty() * Double.valueOf(arrayListOnSale.get(i).getpPrice())) - discount;

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("product_id", arrayListOnSale.get(i).getpId());
                    jsonObject.put("product_price", String.valueOf(arrayListOnSale.get(i).getpPrice()));
                    jsonObject.put("product_qty", formatterQty.format(arrayListOnSale.get(i).getpQty()));
                    jsonObject.put("product_discount_percentage", String.valueOf(arrayListOnSale.get(i).getDiscount()));
                    jsonObject.put("product_final_price", String.valueOf(total).replace(",", ""));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            params.put("credit_note_data", jsonArray.toString());
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Log.d(TAG, "Add CreditNote param " + params.toString());
            Call<AddQuotationModel> call = webApi.addCreditNotesApi(params);
            Log.d(TAG, "Add CreditNote API  " + call.request().toString());
            call.enqueue(new Callback<AddQuotationModel>() {
                @Override
                public void onResponse(Call<AddQuotationModel> call, Response<AddQuotationModel> response) {
                    hidePRD();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            new CToast(getActivity()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();
                            creditNotePlayer.start();
                            arrayListOnSale.clear();
                            mAdapterOnSaleProducts.notifyDataSetChanged();
                            setTotal();

                            //TODO PDF link generation api
                            if (isPrintReceipt) {

                            }

                            txtHomePath.setVisibility(View.VISIBLE);
                            txtSubCatPath.setVisibility(View.GONE);
                            txtProductPath.setVisibility(View.GONE);
                            recyclerviewAllProducts.setVisibility(View.GONE);
                            recyclerviewSubCategory.setVisibility(View.GONE);
                            recyclerviewCategory.setVisibility(View.VISIBLE);

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
                public void onFailure(Call<AddQuotationModel> call, Throwable t) {
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
        tvTitle.setText(getResources().getString(R.string.home));
        arrayListAllProducts.clear();
        arrayListAllProductsTemp.clear();
        arrayListAllProducts.addAll(CandG.arrayListProducts);
        arrayListAllProductsTemp.addAll(CandG.arrayListProducts);
        this.mMyBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null && context != null) {
                    String action = intent.getAction();
                    if (action != null) {
                        if (action.equalsIgnoreCase(context.getResources().getString(R.string.all_product_data))) {
                            arrayListAllProducts.clear();
                            arrayListAllProductsTemp.clear();
                            arrayListAllProducts.addAll(CandG.arrayListProducts);
                            arrayListAllProductsTemp.addAll(CandG.arrayListProducts);
                            stopAnim();
                            swipeRefreshLayout.setRefreshing(false);
                            mAdapterCategory.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.all_product_data)));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextInputEditText edt_product_name = promptsView.findViewById(R.id.edt_product_name);
        final TextInputEditText txt_categoty = promptsView.findViewById(R.id.txt_product_categoty);
        final TextInputEditText edt_base_price = promptsView.findViewById(R.id.edt_base_price);
        final TextInputEditText txt_unit = promptsView.findViewById(R.id.txt_unit);
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
        if (prd != null && !prd.isShowing()) {
            prd.show();
        }
    }

    public void hidePRD() {
        if ((prd != null) && prd.isShowing()) {
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
            new searchProducts(charSequence).execute();
            recyclerviewCategory.setVisibility(View.GONE);
            recyclerviewSubCategory.setVisibility(View.GONE);
            txtSubCatPath.setVisibility(View.GONE);
            txtProductPath.setVisibility(View.GONE);
        }
    }

    class searchProducts extends AsyncTask<Void, Void, Void> {

        String searchWord;

        public searchProducts(String s) {
            searchWord = s.toLowerCase();
            showPRD();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            arrayListAllProductsTemp.clear();
            for (int i = 0; i < arrayListAllProducts.size(); i++) {
                if (arrayListAllProducts.get(i).getpName().toLowerCase().contains(searchWord)) {
                    arrayListAllProductsTemp.add(arrayListAllProducts.get(i));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hidePRD();
            mAdapterProducts = new SaleProducts_Adapter(getActivity(), arrayListAllProductsTemp);
            recyclerviewAllProducts.setAdapter(mAdapterProducts);
            mAdapterProducts.SetOnItemClickListner(new SaleProducts_Adapter.OnItemClickListner() {
                @Override
                public void onItemClick(final int position) {
                    Utils.playClickSound(getActivity());
                    OnSaleProducts_Items availableItem = null;
                    for (int i = 0; i < arrayListOnSale.size(); i++) {
                        if (arrayListAllProductsTemp.get(position).getpId().equals(arrayListOnSale.get(i).getpId())) {
                            availableItem = arrayListOnSale.get(i);
                        }
                    }

                    if (availableItem != null) {
                        availableItem.setpQty(availableItem.getpQty() + 1);
                    } else {
                        OnSaleProducts_Items item = new OnSaleProducts_Items();
                        item.setpId(arrayListAllProductsTemp.get(position).getpId());
                        item.setpName(arrayListAllProductsTemp.get(position).getpName());
                        item.setpQty(1);
                        item.setpPrice(Double.parseDouble(arrayListAllProductsTemp.get(position).getpPrice()));
                        item.setpOpValue(null);
                        item.setCatName(arrayListAllProductsTemp.get(position).getCatName());
                        item.setSubcatName(arrayListAllProductsTemp.get(position).getSubcatName());
                        item.setZero_vat(arrayListAllProductsTemp.get(position).getPzero_vat());
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
        }
    }

    private void setUpItemTouchHelper() {

        try {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                Drawable background;
                Drawable xMark;
                int xMarkMargin;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(getResources().getColor(R.color.red_on_swipe));
                    xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete);
                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.activity_horizontal_margin);
                    initiated = true;
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                    int swipedPosition = viewHolder.getAdapterPosition();
                    boolean isAvailableMoreOption = false;
                    for (int i = 0; i < arrayListOnSale.size(); i++) {
                        if (arrayListOnSale.get(i).getpId().equals(arrayListOnSale.get(swipedPosition).getpId())) {
                            if (i != swipedPosition)
                                isAvailableMoreOption = true;
                        }
                    }

                    OnSaleProducts_Adapter adapter = (OnSaleProducts_Adapter) recyclerviewOnSaleProducts.getAdapter();
                    adapter.remove(swipedPosition);
                    if (arrayListOnSale.size() == 0) {
                        relNoItems.setVisibility(View.VISIBLE);
                        recyclerviewOnSaleProducts.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;

                    if (viewHolder.getAdapterPosition() == -1) {
                        return;
                    }

                    if (!initiated) {
                        init();
                    }

                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    int itemHeight = itemView.getBottom() - itemView.getTop();
                    int intrinsicWidth = xMark.getIntrinsicWidth();
                    int intrinsicHeight = xMark.getIntrinsicWidth();

                    int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                    int xMarkRight = itemView.getRight() - xMarkMargin;
                    int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int xMarkBottom = xMarkTop + intrinsicHeight;
                    xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                    xMark.draw(c);

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }

            };
            ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerviewOnSaleProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpAnimationDecoratorHelper() {
        try {
            recyclerviewOnSaleProducts.addItemDecoration(new RecyclerView.ItemDecoration() {

                Drawable background;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(Color.WHITE);
                    initiated = true;
                }

                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                    if (!initiated) {
                        init();
                    }

                    if (parent.getItemAnimator().isRunning()) {

                        View lastViewComingDown = null;
                        View firstViewComingUp = null;

                        int left = 0;
                        int right = parent.getWidth();

                        int top = 0;
                        int bottom = 0;

                        // find relevant translating views
                        int childCount = parent.getLayoutManager().getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View child = parent.getLayoutManager().getChildAt(i);
                            if (child.getTranslationY() < 0) {
                                // view is coming down
                                lastViewComingDown = child;
                            } else if (child.getTranslationY() > 0) {
                                // view is coming up
                                if (firstViewComingUp == null) {
                                    firstViewComingUp = child;
                                }
                            }
                        }

                        if (lastViewComingDown != null && firstViewComingUp != null) {
                            // views are coming down AND going up to fill the void
                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                        } else if (lastViewComingDown != null) {
                            // views are going down to fill the void
                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                            bottom = lastViewComingDown.getBottom();
                        } else if (firstViewComingUp != null) {
                            // views are coming up to fill the void
                            top = firstViewComingUp.getTop();
                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                        }

                        background.setBounds(left, top, right, bottom);
                        background.draw(c);

                    }
                    super.onDraw(c, parent, state);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
