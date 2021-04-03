package com.flitzen.cng.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import com.flitzen.cng.CandG;
import com.flitzen.cng.Items.OnSaleProducts_Items;
import com.flitzen.cng.Items.SaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.adapter.AllSaleData_Items;
import com.flitzen.cng.adapter.OnSaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleCategory_Adapter;
import com.flitzen.cng.adapter.SaleProducts_Adapter;
import com.flitzen.cng.adapter.SaleSubCat_Items;
import com.flitzen.cng.adapter.SaleSubCategory_Adapter;
import com.flitzen.cng.adapter.Spn_Adapter;
import com.flitzen.cng.model.AddNewProductModel;
import com.flitzen.cng.model.AddQuotationModel;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.model.QuotationDetailsModel;
import com.flitzen.cng.model.SalesPersonModel;
import com.flitzen.cng.model.UnitModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.flitzen.cng.utils.WrapContentLinearLayoutManager;
import com.flitzen.cng.utils.WrapContentStaggedLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quotation_To_Invoice extends AppCompatActivity {

    @BindView(R.id.recyclerview_sales_category)
    RecyclerView recyclerviewCategory;
    @BindView(R.id.recyclerview_sales_sub_category)
    RecyclerView recyclerviewSubCategory;
    @BindView(R.id.recyclerview_sales_products)
    RecyclerView recyclerviewAllProducts;
    @BindView(R.id.recyclerview_sale_on_sale_products)
    RecyclerView recyclerviewOnSaleProducts;
    @BindView(R.id.fab_sales_add_product)
    FloatingActionButton fabAddProduct;
    @BindView(R.id.edt_sale_search_products)
    EditText edtSearchProduct;
    @BindView(R.id.txt_sale_totalitems)
    TextView txtTotalItems;
    @BindView(R.id.view_sale_right_bottom)
    LinearLayout viewBottomRight;
    @BindView(R.id.view_sale_discount)
    LinearLayout viewDiscount;
    @BindView(R.id.txt_sale_totalprice)
    TextView txtTotalPrice;
    @BindView(R.id.txt_sale_discount_amount)
    TextView txtDiscountAmount;
    @BindView(R.id.txt_sale_tax_amount)
    TextView txtTaxAmount;
    @BindView(R.id.txt_sale_grand_total)
    TextView txtGrandTotal;
    @BindView(R.id.txt_exclude_vat)
    TextView txt_exclude_vat;
    @BindView(R.id.btn_sale_create_invoice)
    TextView btnInvoice;
    @BindView(R.id.btn_sale_save_quotation)
    TextView btnSaveQuotation;
    @BindView(R.id.txt_sale_top_home)
    TextView txtHomePath;
    @BindView(R.id.txt_sale_top_sub_cat)
    TextView txtSubCatPath;
    @BindView(R.id.txt_sale_top_products)
    TextView txtProductPath;
    @BindView(R.id.view_sale_clear_items)
    ImageView btnClearList;

    String quotationId, salesPerson, salesPersonId, customerName, customerId, purchase_no;
    SharedPreferences sharedPreferences;
    private SaleCategory_Adapter mAdapterCategory;
    private SaleSubCategory_Adapter mAdapterSubCategory;
    private OnSaleProducts_Adapter mAdapterOnSaleProducts;
    public SaleProducts_Adapter mAdapterProducts;
    private MediaPlayer mediaPlayer, quotationPlayer;
    ArrayList<AllSaleData_Items> arrayListAllData = new ArrayList<>();
    ArrayList<SaleProducts_Items> arrayListAllProducts = new ArrayList<>();
    private ArrayList<OnSaleProducts_Items> arrayListOnSale = new ArrayList<>();
    private ArrayList<SaleProducts_Items> arrayListAllProductsTemp = new ArrayList<>();
    private DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    private DecimalFormat formatterQty = new DecimalFormat(Helper.AMOUNT_FORMATE);
    private ArrayList<SaleSubCat_Items> arrayListSubCategory = new ArrayList<>();
    int selectedSubCatPosition = 0;
    private ProgressDialog prd,prd1;
    private ArrayList<CustomerModel.Result> arrayListCustomer = new ArrayList<>();
    String sale_type = "";
    int salesType = 1;
    String TAG = "Quotation_To_Invoice";
    ArrayList<SalesPersonModel.Result> arrayListSalesPerson = new ArrayList<>();
    ArrayList<UnitModel.Data> arrayListProductUnit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation__to__invoice2);
        ButterKnife.bind(this);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath(getResources().getString(R.string.font_regular))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        getSupportActionBar().setTitle("Quotation to Invoice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        prd = new ProgressDialog(Quotation_To_Invoice.this);
        prd.setMessage("Please wait...");
        prd.setCancelable(false);

        getCustomer(0, 0);
        getSalesPersons(0, null);

        quotationId = getIntent().getStringExtra("q_id");
        sharedPreferences = SharePref.getSharePref(Quotation_To_Invoice.this);

        recyclerviewCategory.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        recyclerviewSubCategory.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        recyclerviewAllProducts.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));

        mAdapterCategory = new SaleCategory_Adapter(Quotation_To_Invoice.this, arrayListAllData);
        recyclerviewCategory.setAdapter(mAdapterCategory);

        mAdapterSubCategory = new SaleSubCategory_Adapter(Quotation_To_Invoice.this, arrayListSubCategory);
        recyclerviewSubCategory.setAdapter(mAdapterSubCategory);

        mAdapterProducts = new SaleProducts_Adapter(Quotation_To_Invoice.this, arrayListAllProductsTemp);
        recyclerviewAllProducts.setAdapter(mAdapterProducts);

        recyclerviewOnSaleProducts.setLayoutManager(new LinearLayoutManager(Quotation_To_Invoice.this));
        mAdapterOnSaleProducts = new OnSaleProducts_Adapter(Quotation_To_Invoice.this, arrayListOnSale);
        recyclerviewOnSaleProducts.setAdapter(mAdapterOnSaleProducts);

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        if (Utils.isOnline(this)) {
            getProductData();
        } else {
            new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }

        mediaPlayer = MediaPlayer.create(Quotation_To_Invoice.this, R.raw.quote_to_invoice_successfully);
        quotationPlayer = MediaPlayer.create(Quotation_To_Invoice.this, R.raw.quote_created_successfully);

        mAdapterProducts.SetOnItemClickListner(new SaleProducts_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(final int position) {
                Utils.playClickSound(getApplicationContext());
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

                    Log.e("=========", arrayListAllProductsTemp.get(position).getPzero_vat());
                }
                mAdapterOnSaleProducts.notifyDataSetChanged();
                setTotal();
            }
        });

        mAdapterOnSaleProducts.SetOnItemClickListner(new OnSaleProducts_Adapter.OnItemClickListner() {
            @Override
            public void updateTotal() {
                Utils.playClickSound(getApplicationContext());
                setTotal();
            }
        });

        mAdapterCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getApplicationContext());
                arrayListSubCategory.clear();
                arrayListSubCategory.addAll(arrayListAllData.get(position).getArrayListSubCat());
                mAdapterSubCategory.notifyDataSetChanged();
                recyclerviewSubCategory.setVisibility(View.VISIBLE);
                recyclerviewCategory.setVisibility(View.GONE);
                txtSubCatPath.setText(arrayListAllData.get(position).getcName());
                txtSubCatPath.setVisibility(View.VISIBLE);
            }
        });

        mAdapterSubCategory.setOnItemClickListener(new SaleCategory_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.playClickSound(getApplicationContext());
                selectedSubCatPosition = position;
                arrayListAllProductsTemp.clear();

                arrayListAllProductsTemp.addAll(arrayListSubCategory.get(position).getArrayListProducts());
                mAdapterProducts.notifyDataSetChanged();

                recyclerviewSubCategory.setVisibility(View.GONE);
                recyclerviewAllProducts.setVisibility(View.VISIBLE);
                txtProductPath.setText(arrayListSubCategory.get(position).getcName());
                txtProductPath.setVisibility(View.VISIBLE);
            }
        });

        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (arrayListCustomer.size() == 0)
                    getCustomer(1, 1);
                else
                    createInvoiceDialog();
            }
        });

        btnSaveQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (arrayListCustomer.size() == 0)
                    getCustomer(1, 2);
                else
                    createQuotationDialog();
            }
        });

        btnClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                new AlertDialog.Builder(Quotation_To_Invoice.this)
                        .setMessage("Are you sure you want to remove all product from sale?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.playClickSound(getApplicationContext());
                                arrayListOnSale.clear();
                                setTotal();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.playClickSound(getApplicationContext());
                            }
                        }).show();

            }
        });

        txtHomePath.setOnClickListener(onTopPathItemClick);
        txtSubCatPath.setOnClickListener(onTopPathItemClick);
        txtProductPath.setOnClickListener(onTopPathItemClick);

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                openAddProduct();
            }
        });
    }

    View.OnClickListener onTopPathItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.playClickSound(getApplicationContext());
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

    public void setTotal() {

        txtTotalItems.setText(arrayListOnSale.size() + "");
        if (arrayListOnSale.size() == 0) {
            viewBottomRight.setVisibility(View.GONE);
        } else {
            viewBottomRight.setVisibility(View.VISIBLE);

            double total = 0.0;
            double discountAmount = 0.0;
            double vatAmount = 0.0;
            double grandTotal = 0.0;
            double exclude_total = 0.0;
            double singleVatAmount = 0.0;

            for (int i = 0; i < arrayListOnSale.size(); i++) {
                double singleTotal = arrayListOnSale.get(i).getpPrice() * arrayListOnSale.get(i).getpQty();
                singleTotal = Double.parseDouble(formatter.format(singleTotal).replace(",", ""));
                Utils.showLog("=== singleTotal " + singleTotal + " price " + arrayListOnSale.get(i).getpPrice() + " qty " + arrayListOnSale.get(i).getpQty());

                double singleDiscount = (singleTotal / 100) * arrayListOnSale.get(i).getDiscount();
                singleDiscount = Double.parseDouble(formatter.format(singleDiscount).replace(",", ""));
                Utils.showLog("=== singleDiscount " + singleDiscount);

                if (arrayListOnSale.get(i).getZero_vat().equals("0")) {
                    singleVatAmount = ((singleTotal - singleDiscount) / 100) * 20;
                    Utils.showLog("=== singleVatAmount " + singleVatAmount);
                    vatAmount += singleVatAmount;
                } else {
                    exclude_total += arrayListOnSale.get(i).getpPrice() * arrayListOnSale.get(i).getpQty();
                    exclude_total = Double.parseDouble(formatter.format(exclude_total).replace(",", ""));
                    Utils.showLog("=== exclude_total " + exclude_total);
                }

                total += singleTotal;
                discountAmount += singleDiscount;
                grandTotal = total + vatAmount - discountAmount;


            }

            if (discountAmount > 0)
                viewDiscount.setVisibility(View.VISIBLE);
            else
                viewDiscount.setVisibility(View.GONE);

            double taxRate = Double.parseDouble("20");

            txtTotalPrice.setText(getResources().getString(R.string.pound) + " " + formatter.format(total - discountAmount));
            txtDiscountAmount.setText(getResources().getString(R.string.pound) + formatter.format(discountAmount));
            txtTaxAmount.setText(getResources().getString(R.string.pound) + formatter.format(vatAmount));

            txt_exclude_vat.setText(getResources().getString(R.string.pound) + formatter.format(exclude_total));
            txtGrandTotal.setText(getResources().getString(R.string.pound) + formatter.format(grandTotal));
        }
    }

    public void openAddProduct() {

        LayoutInflater localView = LayoutInflater.from(Quotation_To_Invoice.this);
        View promptsView = localView.inflate(R.layout.dialog_add_product, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Quotation_To_Invoice.this);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText edt_product_name = promptsView.findViewById(R.id.edt_product_name);
        final TextView txt_categoty = promptsView.findViewById(R.id.txt_product_categoty);
        final EditText edt_base_price = promptsView.findViewById(R.id.edt_base_price);
        final TextView txt_unit = promptsView.findViewById(R.id.txt_unit);
        ImageView img_close = promptsView.findViewById(R.id.img_close);
        Button btn_cancel = promptsView.findViewById(R.id.btn_cancel);
        alertDialog.setCanceledOnTouchOutside(false);

        txt_categoty.setVisibility(View.GONE);
        getProductUnit(txt_unit);
        Button btn_add =  promptsView.findViewById(R.id.btn_add);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getApplicationContext());
                alertDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                alertDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(Quotation_To_Invoice.this);
                if (edt_product_name.getText().toString().trim().isEmpty()) {
                    edt_product_name.setError("Enter product name");
                    edt_product_name.requestFocus();
                    return;
                } else if (edt_base_price.getText().toString().equals("")) {
                    edt_base_price.setError("Enter base price");
                    edt_base_price.requestFocus();
                    return;
                } else if (txt_unit.getText().toString().equals("Select Product Unit")) {
                    new CToast(Quotation_To_Invoice.this).simpleToast("Please select a product unit.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                    return;
                } else {
                    if (Utils.isOnline(Quotation_To_Invoice.this)) {
                        addNewProduct(edt_product_name.getText().toString(), edt_base_price.getText().toString(), txt_unit.getTag().toString(), alertDialog);
                    } else {
                        new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }
            }
        });

        alertDialog.show();
    }

    private void addNewProduct(String productName, String price, String unitId, AlertDialog alertDialog) {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<AddNewProductModel> call = webApi.addNewProduct(getResources().getString(R.string.api_key), sharedPreferences.getString(SharePref.USERID, ""), productName, price, unitId);
            call.enqueue(new Callback<AddNewProductModel>() {
                @Override
                public void onResponse(Call<AddNewProductModel> call, Response<AddNewProductModel> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getStatus()==1){
                            new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();
                            String selectedProduct = response.body().getProduct_id();
                            SaleProducts_Items item = new SaleProducts_Items();
                            item.setpId(selectedProduct);
                            item.setSelected(true);
                            item.setpPrice(price);
                            item.setpName(productName);
                            arrayListAllProducts.add(item);

                            OnSaleProducts_Items mItem = new OnSaleProducts_Items();
                            mItem.setpId(item.getpId());
                            mItem.setpName(item.getpName());
                            mItem.setpQty(1);
                            mItem.setZero_vat("0");
                            mItem.setpPrice(Double.parseDouble(item.getpPrice()));
                            arrayListOnSale.add(mItem);
                            mAdapterOnSaleProducts.notifyDataSetChanged();

                            setTotal();
                            hidePRD();
                            alertDialog.dismiss();
                        }
                    } else {
                        new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<AddNewProductModel> call, Throwable t) {
                    new CToast(Quotation_To_Invoice.this).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            new CToast(Quotation_To_Invoice.this).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
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
                            if(response.body().getStatus()==1){
                                arrayListCustomer.clear();
                                arrayListCustomer.addAll(response.body().getData());
                                if (btnType == 1) {
                                    createInvoiceDialog();
                                } else if (btnType == 2) {
                                    createQuotationDialog();
                                }
                            }
                        } else {
                            if (checkClick == 1) {
                                new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                        .setBackgroundColor(R.color.msg_fail)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerModel> call, Throwable t) {
                        if (checkClick == 1) {
                            new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                        hidePRD();
                    }
                });
            }
        }).start();
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
                            if(response.body().getStatus()==1){
                                arrayListSalesPerson.clear();
                                arrayListSalesPerson.addAll(response.body().getData());
                                if (checkClick == 1) {
                                    selectSalesPerson(textView);
                                }
                            }

                        } else {
                            if (checkClick == 1) {
                                new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                        .setBackgroundColor(R.color.msg_fail)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SalesPersonModel> call, Throwable t) {
                        if (checkClick == 1) {
                            new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
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
        LayoutInflater localView = LayoutInflater.from(Quotation_To_Invoice.this);
        View promptsView = localView.inflate(R.layout.dialog_spinner, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Quotation_To_Invoice.this);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edtSearchUnit = (EditText) promptsView.findViewById(R.id.edt_spn_search);
        final ListView list_Unit = (ListView) promptsView.findViewById(R.id.list_spn);

        edtSearchUnit.setVisibility(View.GONE);
        edtSearchUnit.setHint("Search Sales Person");

        final ArrayList<String> arrayListTemp = new ArrayList<>();
        for (int i = 0; i < arrayListSalesPerson.size(); i++) {
            arrayListTemp.add(arrayListSalesPerson.get(i).getName());
        }
        list_Unit.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));

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
                    list_Unit.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));
                } else {
                    arrayListTemp.clear();
                    for (int i = 0; i < arrayListSalesPerson.size(); i++) {
                        arrayListTemp.add(arrayListSalesPerson.get(i).getName());
                    }
                    list_Unit.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list_Unit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Utils.playClickSound(getApplicationContext());
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

    private void getProductData() {
        try {
            showPRD("Getting quotation details...");
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<AllProductDataModel> call = webApi.allProductApi(getResources().getString(R.string.api_key));

            call.enqueue(new retrofit2.Callback<AllProductDataModel>() {
                @Override
                public void onResponse(Call<AllProductDataModel> call, retrofit2.Response<AllProductDataModel> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            arrayListAllData.clear();
                            arrayListAllProducts.clear();
                            CandG.arrayListAllData.clear();
                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                AllSaleData_Items item = new AllSaleData_Items();
                                item.setcId(response.body().getResult().get(i).getCategoryId());
                                item.setcName(response.body().getResult().get(i).getName());
                                ArrayList<SaleSubCat_Items> arrayListSubCat = new ArrayList<>();
                                if (response.body().getResult().get(i).getSubcategory() != null) {
                                    for (int j = 0; j < response.body().getResult().get(i).getSubcategory().size(); j++) {
                                        SaleSubCat_Items itemSub = new SaleSubCat_Items();
                                        itemSub.setcId(response.body().getResult().get(i).getSubcategory().get(j).getCategoryId());
                                        itemSub.setcName(response.body().getResult().get(i).getSubcategory().get(j).getName());
                                        ArrayList<SaleProducts_Items> arrayListProducts = new ArrayList<>();
                                        if (response.body().getResult().get(i).getSubcategory().get(j).getProduct() != null) {
                                            for (int k = 0; k < response.body().getResult().get(i).getSubcategory().get(j).getProduct().size(); k++) {
                                                AllProductDataModel.Product product = response.body().getResult().get(i).getSubcategory().get(j).getProduct().get(k);
                                                SaleProducts_Items itemProduct = new SaleProducts_Items();
                                                itemProduct.setpId(product.getProductId());
                                                itemProduct.setpName(product.getName().toUpperCase());
                                                itemProduct.setpPrice(product.getPrice());
                                                itemProduct.setPzero_vat(product.getZeroVat());
                                                itemProduct.setCatName(response.body().getResult().get(i).getName());
                                                itemProduct.setSubcatName(response.body().getResult().get(i).getSubcategory().get(j).getName());
                                                arrayListProducts.add(itemProduct);
                                                CandG.arrayListProducts.add(itemProduct);
                                                arrayListAllProducts.add(itemProduct);
                                            }
                                        }

                                        itemSub.setArrayListProducts(arrayListProducts);
                                        arrayListSubCat.add(itemSub);
                                    }
                                }

                                item.setArrayListSubCat(arrayListSubCat);
                                CandG.arrayListAllData.add(item);
                                arrayListAllData.add(item);
                            }
                            mAdapterCategory.notifyDataSetChanged();
                            // JsonCache.SALE_ALL_DATA = result;
                            getQuotationDetails();
                        } else {
                            new CToast(getApplicationContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } else {
                        new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<AllProductDataModel> call, Throwable t) {
                    hidePRD();
                    new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            hidePRD();
            new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    private void getQuotationDetails() {
        WebApi webApi = CandG.getClient().create(WebApi.class);
        Call<QuotationDetailsModel> call = webApi.quotationDetails(getResources().getString(R.string.api_key), quotationId);
        Log.d(TAG, "GET Quotation DETAILS API  " + call.request().toString());
        call.enqueue(new Callback<QuotationDetailsModel>() {
            @Override
            public void onResponse(Call<QuotationDetailsModel> call, Response<QuotationDetailsModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        getSupportActionBar().setSubtitle("Invoice number - " + response.body().getData().getQuotationId());
                        salesType = Integer.parseInt(response.body().getData().getSalesType());
                        System.out.println("==========salesType   " + salesType);
                        customerId = response.body().getData().getCustomerId();
                        customerName = response.body().getData().getQuotationTo();
                        salesPerson = response.body().getData().getSalesPersonName();
                        salesPersonId = response.body().getData().getSalesPersonId();
                        purchase_no = response.body().getData().getPurchaseNo();

                        for (int i = 0; i < response.body().getData().getQuotationData().size(); i++) {
                            QuotationDetailsModel.QuotationProductData productData = response.body().getData().getQuotationData().get(i);
                            OnSaleProducts_Items item = new OnSaleProducts_Items();
                            item.setpId(productData.getProductId());
                            item.setpName(productData.getProductName());
                            item.setpQty(Double.parseDouble(productData.getProductQty()));
                            item.setpPrice(Double.parseDouble(productData.getProductPrice()));
                            item.setCatName(productData.getProductCategoryName());
                            item.setSubcatName(productData.getProductSubcategoryName());
                            item.setDiscount(Double.parseDouble(productData.getProductDiscountPercentage()));
                            item.setZero_vat(productData.getZeroVat());

                            arrayListOnSale.add(item);
                        }
                        hidePRD1();
                        mAdapterOnSaleProducts.notifyDataSetChanged();
                        setTotal();

                    } else {
                        hidePRD1();
                        new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                } else {
                    hidePRD1();
                    new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<QuotationDetailsModel> call, Throwable t) {
                hidePRD1();
                new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();

            }
        });
    }

    public void createQuotationDialog() {
        LayoutInflater localView = LayoutInflater.from(Quotation_To_Invoice.this);
        View promptsView = localView.inflate(R.layout.dialog_create_quotation, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Quotation_To_Invoice.this);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final View viewAccountSale = promptsView.findViewById(R.id.view_create_quo_account_sale);
        final View viewCashSale = promptsView.findViewById(R.id.view_create_quo_cash_sale);
        final ToggleSwitch toggleSwitch = promptsView.findViewById(R.id.toggle_create_quo);
        final TextInputEditText txtSelectCustomer = promptsView.findViewById(R.id.txt_create_quo_select_cutomer);
        final TextInputEditText edtCustomerName = promptsView.findViewById(R.id.edt_create_quo_select_cutomer);
        final TextInputEditText edtPo = promptsView.findViewById(R.id.edt_create_quo_po);
        final ImageView img_close = promptsView.findViewById(R.id.img_close);
        TextView txtQuoTotalItems = promptsView.findViewById(R.id.txt_create_quo_total_items);
        TextView txtQuoTotalAmount = promptsView.findViewById(R.id.txt_create_quo_total_amount);
        View btnSave = promptsView.findViewById(R.id.btn_create_quo_save);
        View btnSavePrint = promptsView.findViewById(R.id.btn_create_quo_save_and_print);
        final TextInputEditText txtSelectSalesPerson = promptsView.findViewById(R.id.txt_create_quo_select_sales_person);

        toggleSwitch.setVisibility(View.GONE);

        txtSelectSalesPerson.setTag(salesPersonId);
        txtSelectSalesPerson.setText(salesPerson);
        txtSelectSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getApplicationContext());
                if (arrayListSalesPerson.size() == 0) getSalesPersons(1, txtSelectSalesPerson);
                else selectSalesPerson(txtSelectSalesPerson);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getApplicationContext());
                alertDialog.dismiss();
            }
        });

        if (salesType == 0) {
            viewAccountSale.setVisibility(View.VISIBLE);
            viewCashSale.setVisibility(View.GONE);
            txtSelectCustomer.setText(customerName);
            txtSelectCustomer.setTag(customerId);
            edtPo.setText(purchase_no);

            toggleSwitch.setCheckedTogglePosition(0);
        } else {
            viewCashSale.setVisibility(View.VISIBLE);
            viewAccountSale.setVisibility(View.GONE);
            edtCustomerName.setText(customerName);

            edtPo.setText(purchase_no);

            txtSelectCustomer.setTag("0");
            toggleSwitch.setCheckedTogglePosition(1);
        }

        txtQuoTotalItems.setText(txtTotalItems.getText());
        txtQuoTotalAmount.setText(txtGrandTotal.getText());

        txtSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                selectCustomer(txtSelectCustomer);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Customer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                alertDialog.dismiss();
                createQuatation(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(), edtCustomerName.getText().toString().trim(),
                        false, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
            }
        });

        btnSavePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (toggleSwitch.getCheckedTogglePosition() == 0) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Customer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                alertDialog.dismiss();
                createQuatation(toggleSwitch.getCheckedTogglePosition(), txtSelectCustomer.getTag().toString(), edtCustomerName.getText().toString().trim(),
                        true, txtSelectSalesPerson.getTag().toString(), edtPo.getText().toString());
            }
        });

        alertDialog.show();
    }

    String paymentType = "", deliveryType = "";
    double paidAMount = 0;

    public void createInvoiceDialog() {
        paymentType = "";
        deliveryType = "";

        LayoutInflater localView = LayoutInflater.from(Quotation_To_Invoice.this);
        View promptsView = localView.inflate(R.layout.dialog_create_invoice_from_quotation, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Quotation_To_Invoice.this);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.9f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alertDialog.getWindow().setAttributes(layoutParams);

        final View viewAccountSale = promptsView.findViewById(R.id.view_create_inv_account_sale);
        final View viewCashSaleTogle = promptsView.findViewById(R.id.view_create_inv_cashsale);
        final TextInputEditText txtSelectCustomer = promptsView.findViewById(R.id.txt_create_inv_select_cutomer);
        final TextInputEditText edtCustomerName = promptsView.findViewById(R.id.edt_create_inv_cutomer);
        final TextInputLayout edt_create_inv_select_cutomer_main = promptsView.findViewById(R.id.edt_create_inv_select_cutomer_main);
        final View btnSave = promptsView.findViewById(R.id.btn_create_inv_save);
        final View btnSavePrint = promptsView.findViewById(R.id.btn_create_inv_save_and_print);
        final View btnSaveDelivery = promptsView.findViewById(R.id.btn_create_inv_save_and_delivery_note);
        final View btnBoth = promptsView.findViewById(R.id.btn_create_inv_both);
        final ImageView img_close = promptsView.findViewById(R.id.img_close);

        final ToggleSwitch toggleSwitch = promptsView.findViewById(R.id.toggle_create_inv);

        final TextInputEditText edtPo = promptsView.findViewById(R.id.edt_create_inv_po);
        final TextInputEditText edtAddress = promptsView.findViewById(R.id.edt_create_inv_address);
        final TextInputEditText edtPhone = promptsView.findViewById(R.id.edt_create_inv_phone);
        final TextInputEditText etPinCode = promptsView.findViewById(R.id.edt_create_inv_pin_code);
        final TextInputEditText edtPaidAmount = promptsView.findViewById(R.id.edt_create_inv_paid_amount);
        final TextInputEditText edtNote = promptsView.findViewById(R.id.edt_create_inv_note);
        final RadioGroup rdbGroup = promptsView.findViewById(R.id.radio_grp_create_inv_delivery_type);
        final View viewReturnAmount = promptsView.findViewById(R.id.view_create_inv_return_amount);
        final View viewCashSale = promptsView.findViewById(R.id.view_create_inv_cash_sale);
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

        txtSelectSalesPerson.setText(salesPerson);
        txtSelectSalesPerson.setTag(salesPersonId);

        txtSelectSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getApplicationContext());
                if (arrayListSalesPerson.size() == 0) getSalesPersons(1, txtSelectSalesPerson);
                else selectSalesPerson(txtSelectSalesPerson);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getApplicationContext());
                alertDialog.dismiss();
            }
        });

        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Utils.playClickSound(getApplicationContext());
                if (position == 0) {
                    viewAccountSale.setVisibility(View.VISIBLE);
                    viewCashSale.setVisibility(View.GONE);
                    viewCashSaleTogle.setVisibility(View.GONE);
                    sale_type = "account";
                    edtCustomerName.setEnabled(false);
                    edt_create_inv_select_cutomer_main.setVisibility(View.GONE);
                    salesType = 1;

                } else {
                    viewCashSale.setVisibility(View.VISIBLE);
                    viewCashSaleTogle.setVisibility(View.VISIBLE);
                    viewAccountSale.setVisibility(View.GONE);
                    sale_type = "case";
                    edtCustomerName.setEnabled(true);
                    edt_create_inv_select_cutomer_main.setVisibility(View.VISIBLE);
                    salesType = 2;

                }
            }
        });


        edtCustomerName.setText(customerName);
        if (salesType == 1) {//Account sale
            edtCustomerName.setTag(customerId);
            edtCustomerName.setEnabled(false);
            toggleSwitch.setCheckedTogglePosition(0);
            viewAccountSale.setVisibility(View.VISIBLE);
            viewCashSale.setVisibility(View.GONE);
            viewCashSaleTogle.setVisibility(View.GONE);

            txtSelectCustomer.setText(customerName);
            txtSelectCustomer.setTag(customerId);
        } else {//Cash sale
            edtCustomerName.setText(customerName);
            edtCustomerName.setEnabled(true);
            toggleSwitch.setCheckedTogglePosition(1);
            viewAccountSale.setVisibility(View.GONE);
            viewCashSale.setVisibility(View.VISIBLE);
            viewCashSaleTogle.setVisibility(View.VISIBLE);

            txtSelectCustomer.setTag("0");
        }

        if (!purchase_no.equals("")) {
            edtPo.setText(purchase_no);
        }

        paidAMount = Double.parseDouble(txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
        edtPaidAmount.setText(txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));

        rdbCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b) {
                    btnSaveDelivery.setVisibility(View.GONE);
                    deliveryType = "1";
                }
            }
        });
        rdbDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b) {
                    btnSaveDelivery.setVisibility(View.VISIBLE);
                    deliveryType = "2";
                }
            }
        });
        rdbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "1";
                //0= Cash, 1 = Card, 2 = Debit 3= Online, 4 = cheque, 5 =other
            }
        });

        rdbCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "2";
            }
        });
        rdbDebit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "3";
            }
        });
        rdbOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "4";
            }
        });
        rdbCheque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "5";
            }
        });
        rdbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.playClickSound(getApplicationContext());
                if (b)
                    paymentType = "6";
            }
        });

        edtPaidAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (charSequence.toString().length() > 0) {
                        double enteredAmount = Double.parseDouble(edtPaidAmount.getText().toString().trim());
                        double returnAmount = enteredAmount - paidAMount;
                        txtReturnAmount.setText(formatter.format(returnAmount));
                        if (returnAmount == 0) {
                            viewReturnAmount.setVisibility(View.GONE);
                        } else {
                            viewReturnAmount.setVisibility(View.VISIBLE);
                        }

                        if (returnAmount < 0) {
                            btnSave.setEnabled(false);
                            btnSavePrint.setEnabled(false);
                        } else {
                            btnSave.setEnabled(true);
                            btnSavePrint.setEnabled(true);
                        }

                    } else {
                        viewReturnAmount.setVisibility(View.GONE);
                    }
                }catch (NumberFormatException e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                selectCustomer(txtSelectCustomer);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (salesType == 1) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(Quotation_To_Invoice.this).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == 2) {
                        if (edtAddress.getText().toString().trim().isEmpty()) {
                            edtAddress.setError("Enter Delivery Address");
                            edtAddress.requestFocus();
                            return;
                        }
                        if (edtPhone.getText().toString().trim().isEmpty()) {
                            edtPhone.setError("Enter Phone Number");
                            edtPhone.requestFocus();
                            return;
                        }
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_id", customerId);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, false, false, false);

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    } else {

                        Map<String, String> params = new HashMap<>();
                        params.put("customer_name", edtCustomerName.getText().toString().trim());
                        params.put("sales_type", String.valueOf(salesType));
                        params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                        params.put("payment_mode", paymentType);
                        params.put("purchase_no", edtPo.getText().toString().trim());
                        params.put("delivery_type", deliveryType);
                        params.put("delivery_address", edtAddress.getText().toString().trim());
                        params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                        params.put("delivery_pincode", etPinCode.getText().toString().trim());
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
                Utils.playClickSound(getApplicationContext());
                if (salesType == 1) {
                    if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                        new CToast(Quotation_To_Invoice.this).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == 2) {
                        if (edtAddress.getText().toString().trim().isEmpty()) {
                            edtAddress.setError("Enter Delivery Address");
                            edtAddress.requestFocus();
                            return;
                        }
                        if (edtPhone.getText().toString().trim().isEmpty()) {
                            edtPhone.setError("Enter Phone Number");
                            edtPhone.requestFocus();
                            return;
                        }
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_id", customerId);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, true, false, false);

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_name", edtCustomerName.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                    params.put("payment_mode", paymentType);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("paid", edtPaidAmount.getText().toString().trim());
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, true, false, false);
                }
            }
        });


        btnSaveDelivery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (txtSelectCustomer.getText().toString().trim().isEmpty()) {
                    new CToast(Quotation_To_Invoice.this).simpleToast("Select Customer", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                    return;
                }
                if (salesType == 1) {
                    if (rdbGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == 2) {
                        if (edtAddress.getText().toString().trim().isEmpty()) {
                            edtAddress.setError("Enter Delivery Address");
                            edtAddress.requestFocus();
                            return;
                        }
                        if (edtPhone.getText().toString().trim().isEmpty()) {
                            edtPhone.setError("Enter Phone Number");
                            edtPhone.requestFocus();
                            return;
                        }
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_id", customerId);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, false, true, false);

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_name", edtCustomerName.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                    params.put("payment_mode", paymentType);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("paid", edtPaidAmount.getText().toString().trim());
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, false, true, false);
                }
            }
        });

        btnBoth.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getApplicationContext());
                if (salesType == 1) {
                    if (rdbGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Delivery Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rdbGroup.getCheckedRadioButtonId() == 2) {
                        if (edtAddress.getText().toString().trim().isEmpty()) {
                            edtAddress.setError("Enter Delivery Address");
                            edtAddress.requestFocus();
                            return;
                        }
                        if (edtPhone.getText().toString().trim().isEmpty()) {
                            edtPhone.setError("Enter Phone Number");
                            edtPhone.requestFocus();
                            return;
                        }
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("customer_id", customerId);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, false, false, true);

                } else {
                    if (paymentType == null || paymentType.isEmpty() || paymentType.equals("null")) {
                        Toast.makeText(Quotation_To_Invoice.this, "Select Payment Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtPaidAmount.getText().toString().trim().isEmpty()) {
                        edtPaidAmount.setError("Enter Paid Amount");
                        edtPaidAmount.requestFocus();
                        return;
                    }

                    Map<String, String> params = new HashMap<>();
                    params.put("sales_name", edtCustomerName.getText().toString().trim());
                    params.put("sales_type", String.valueOf(salesType));
                    params.put("due", txtReturnAmount.getText().toString().trim().replace(",", ""));
                    params.put("payment_mode", paymentType);
                    params.put("purchase_no", edtPo.getText().toString().trim());
                    params.put("delivery_type", deliveryType);
                    params.put("delivery_address", edtAddress.getText().toString().trim());
                    params.put("delivery_phone_number", edtPhone.getText().toString().trim());
                    params.put("delivery_pincode", etPinCode.getText().toString().trim());
                    params.put("paid", edtPaidAmount.getText().toString().trim());
                    params.put("invoice_notes", edtNote.getText().toString().trim());
                    params.put("sales_person_id", txtSelectSalesPerson.getTag().toString());

                    alertDialog.dismiss();
                    createInvoice(params, false, false, true);
                }
            }
        });
    }

    public void createInvoice(Map<String, String> params, final boolean isPrintReceipt,
                              final boolean isDeliveryNote, final boolean isPrintBoth) {
        SharedPreferences sharedPreferences = SharePref.getSharePref(Quotation_To_Invoice.this);
        showPRD();
        params.put("api_key", getResources().getString(R.string.api_key));
        params.put("quotation_id", quotationId);
        params.put("user_id", sharedPreferences.getString(SharePref.USERID, ""));
        params.put("total_amount", txtTotalPrice.getText().toString().trim().substring(1, txtTotalPrice.getText().toString().trim().length()).replace(",", ""));
        params.put("final_total", txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
        params.put("vat_amount", txtTaxAmount.getText().toString().trim().substring(1, txtTaxAmount.getText().toString().trim().length()).replace(",", ""));
        params.put("discount_amount", txtDiscountAmount.getText().toString().trim().substring(1, txtDiscountAmount.getText().toString().trim().length()).replace(",", ""));
        params.put("zero_vat_total", txt_exclude_vat.getText().toString().trim().substring(1, txt_exclude_vat.getText().toString().trim().length()));

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
        Call<AddQuotationModel> call = webApi.convertQuotationToInvoiceApi(params);
        Log.d(TAG, "Add Invoice API  " + call.request().toString());
        call.enqueue(new Callback<AddQuotationModel>() {
            @Override
            public void onResponse(Call<AddQuotationModel> call, Response<AddQuotationModel> response) {
                hidePRD();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        mediaPlayer.start();
                        new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
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
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                } else {
                    new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AddQuotationModel> call, Throwable t) {
                hidePRD();
                new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();
            }
        });
    }

    public void createQuatation(final int type, final String customerId, final String customerName,
                                final boolean isPrintReceipt, final String salesPerson, final String po) {
        try {
            showPRD();
            Map<String, String> params = new HashMap<>();
            params.put("api_key", getResources().getString(R.string.api_key));
            params.put("quotation_id", quotationId);
            params.put("user_id", sharedPreferences.getString(SharePref.USERID, ""));
            if(String.valueOf(type).equalsIgnoreCase("1")){
                params.put("customer_id", customerId);
            }
            params.put("customer_name", customerName);
            params.put("sales_person_id", salesPerson);
            params.put("total_amount", txtTotalPrice.getText().toString().trim().substring(1, txtTotalPrice.getText().toString().trim().length()).replace(",", ""));
            params.put("vat_amount", txtTaxAmount.getText().toString().trim().substring(1, txtTaxAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("final_total", txtGrandTotal.getText().toString().trim().substring(1, txtGrandTotal.getText().toString().trim().length()).replace(",", ""));
            params.put("sales_type", String.valueOf(type));
            params.put("purchase_no", po);
            params.put("discount_amount", txtDiscountAmount.getText().toString().trim().substring(1, txtDiscountAmount.getText().toString().trim().length()).replace(",", ""));
            params.put("zero_vat_total", txt_exclude_vat.getText().toString().trim().substring(1, txt_exclude_vat.getText().toString().trim().length()));
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
            Call<AddQuotationModel> call = webApi.editQuotationApi(params);
            Log.d(TAG, "Add Quotation API  " + call.request().toString());
            call.enqueue(new Callback<AddQuotationModel>() {
                @Override
                public void onResponse(Call<AddQuotationModel> call, Response<AddQuotationModel> response) {
                    hidePRD();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();

                            quotationPlayer.start();

                            //TODO PDF link generation api
                            if (isPrintReceipt) {

                            }
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } else {
                        new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<AddQuotationModel> call, Throwable t) {
                    hidePRD();
                    new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();

                }
            });
        } catch (Exception e) {
            hidePRD();
            new CToast(Quotation_To_Invoice.this).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    public void selectCustomer(final TextView txt) {
        LayoutInflater localView = LayoutInflater.from(Quotation_To_Invoice.this);
        View promptsView = localView.inflate(R.layout.dialog_spinner, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Quotation_To_Invoice.this);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final EditText edtSearchLocation = promptsView.findViewById(R.id.edt_spn_search);
        final ListView list_location = promptsView.findViewById(R.id.list_spn);

        final ArrayList<String> arrayListTemp = new ArrayList<>();
        for (int i = 0; i < arrayListCustomer.size(); i++) {
            arrayListTemp.add(arrayListCustomer.get(i).getName());
        }
        list_location.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));

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
                    list_location.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));
                } else {
                    arrayListTemp.clear();
                    for (int i = 0; i < arrayListCustomer.size(); i++) {
                        arrayListTemp.add(arrayListCustomer.get(i).getName());
                    }
                    list_location.setAdapter(new Spn_Adapter(Quotation_To_Invoice.this, arrayListTemp));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Utils.playClickSound(getApplicationContext());
                for (int i = 0; i < arrayListCustomer.size(); i++) {
                    if (arrayListCustomer.get(i).getName().toLowerCase().equals(arrayListTemp.get(position).toLowerCase())) {
                        String selectedCustomer = arrayListCustomer.get(i).getCustomerId();
                        customerId = arrayListCustomer.get(i).getCustomerId();

                        if (arrayListTemp.get(position).equals("Add New Customer")) {
                            //TODO in future
                            //openAddCustomer(txt);
                        } else {
                            txt.setText(arrayListTemp.get(position));
                            txt.setTag(selectedCustomer);
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

    public void getProductUnit(final TextView txt_unit) {
        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<UnitModel> call = webApi.getUnitList(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<UnitModel>() {
                @Override
                public void onResponse(Call<UnitModel> call, Response<UnitModel> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getStatus()==1){
                            arrayListProductUnit.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayListProductUnit.add(response.body().getData().get(i));
                            }
                            if (arrayListProductUnit.size() > 0) {
                                txt_unit.setText(arrayListProductUnit.get(0).getUnitName());
                                txt_unit.setTag(arrayListProductUnit.get(0).getUnitId());
                            }
                        }
                    } else {
                        new CToast(Quotation_To_Invoice.this).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<UnitModel> call, Throwable t) {
                    new CToast(Quotation_To_Invoice.this).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        } catch (Exception e) {
            new CToast(Quotation_To_Invoice.this).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    public void showPRD(String message) {
        prd1 = new ProgressDialog(Quotation_To_Invoice.this);
        prd1.setMessage(message);
        prd1.setCancelable(false);
        prd1.show();
    }

    public void hidePRD1(){
        if ((prd1 != null) && prd1.isShowing()) {
            prd1.dismiss();
        }
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

    private void setUpItemTouchHelper() {

        try {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                Drawable background;
                Drawable xMark;
                int xMarkMargin;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(getResources().getColor(R.color.red_on_swipe));
                    xMark = ContextCompat.getDrawable(Quotation_To_Invoice.this, R.drawable.ic_delete);
                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    xMarkMargin = (int) Quotation_To_Invoice.this.getResources().getDimension(R.dimen.activity_horizontal_margin);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.playClickSound(getApplicationContext());
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}