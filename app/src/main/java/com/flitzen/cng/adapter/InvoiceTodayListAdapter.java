package com.flitzen.cng.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.fragment.Invoice_ListFragment;
import com.flitzen.cng.model.CommonModel;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceTodayListAdapter extends RecyclerView.Adapter<InvoiceTodayListAdapter.ViewHolder> {

    Context context;
    ArrayList<TodayInvoiceListingModel.Result> arrayList;
    DecimalFormat numberFotmate = new DecimalFormat(Helper.AMOUNT_FORMATE);
    String name = "", invoice_id = "", pending_amount = "";
    SharedPreferences sharedPreferences;
    android.app.AlertDialog alertDialog;
    Invoice_ListFragment invoice_listFragment;

    public InvoiceTodayListAdapter(Context context, ArrayList<TodayInvoiceListingModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_invoice_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!arrayList.get(position).getPaid().trim().equals("")) {
            holder.txtPaidAmount.setText(context.getResources().getString(R.string.pound) + " " + numberFotmate.format(Double.valueOf(arrayList.get(position).getPaid())));
        } else {
            holder.txtPaidAmount.setText("-");
        }
        if (!arrayList.get(position).getDue().trim().equals("")) {
            holder.txtDueAmount.setText(context.getResources().getString(R.string.pound) + " " + numberFotmate.format(Double.valueOf(arrayList.get(position).getDue())));
        } else {
            holder.txtDueAmount.setText("-");
        }

        holder.txtPrice.setText(context.getResources().getString(R.string.pound) + " " + numberFotmate.format(Double.valueOf(arrayList.get(position).getFinalTotal())));
        holder.txtSubTotal.setText(context.getResources().getString(R.string.pound) + " " + numberFotmate.format(Double.valueOf(arrayList.get(position).getTotalAmount())));
        holder.txtVatAmount.setText(context.getResources().getString(R.string.pound) + " " + numberFotmate.format(Double.valueOf(arrayList.get(position).getVatAmount())));
        holder.txtNo.setText("CG-" + arrayList.get(position).getInvoiceId());
        holder.txtCustomer.setText(arrayList.get(position).getInvoiceTo());
        if (Helper.getCurrentDate("dd MMMM, yyyy").toUpperCase()
                .equals(arrayList.get(position).getInvoiceDate().toUpperCase())) {
        }

        holder.txtDate.setText(arrayList.get(position).getInvoiceDate());
        holder.txtTime.setText(arrayList.get(position).getInvoiceTime());

        if (!arrayList.get(position).getPurchaseNo().equals("")) {
            holder.txtPONo.setText(arrayList.get(position).getPurchaseNo());
        } else {
            holder.txtPONo.setText("-");
        }

        if (arrayList.get(position).getSalesType() == null) {
            holder.txtStatus.setVisibility(View.INVISIBLE);
        } else if (arrayList.get(position).getSalesType().equals("2")) {
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.INVISIBLE);
        }

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                showMoreOption(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrice, txtNo, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount, txtPaidAmount, txtDueAmount, txtPONo;
        CardView mainView;
        LinearLayout txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice = itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtNo = itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus = itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer = itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate = itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime = itemView.findViewById(R.id.txt_invoicelist_a_time);
            txtSubTotal = itemView.findViewById(R.id.txt_invoicelist_a_c_sub_total);
            txtVatAmount = itemView.findViewById(R.id.txt_invoicelist_a_c_vat_amount);
            txtPaidAmount = itemView.findViewById(R.id.txt_invoicelist_a_paid);
            txtDueAmount = itemView.findViewById(R.id.txt_invoicelist_a_due);
            txtPONo = itemView.findViewById(R.id.txt_invoicelist_a_po_no);
        }
    }

    public void showMoreOption(final int position) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_invoice_list_option, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        View viewDeliveryNote = promptsView.findViewById(R.id.view_inv_dialog_delivery_note);
        View viewPdf = promptsView.findViewById(R.id.view_inv_dialog_view_pdf);
        View viewAddPayment = promptsView.findViewById(R.id.view_inv_dialog_add_payment);
        View viewSendMail = promptsView.findViewById(R.id.view_inv_dialog_send_mail);

        pending_amount = arrayList.get(position).getDue();
        invoice_id = arrayList.get(position).getInvoiceId();

        if (!arrayList.get(position).getInvoiceTo().equals("-")) {
            name = arrayList.get(position).getInvoiceTo();
        } else {
            name = "Sir/Madam";
        }

        if (arrayList.get(position).getPaymentStatus().equalsIgnoreCase("2")) {
            viewAddPayment.setVisibility(View.GONE);
        }

        viewAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
                openAddPayment();
            }
        });


        alertDialog.show();
    }

    public void openAddPayment() {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_add_payment, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView txtCustomer = promptsView.findViewById(R.id.txt_add_payment_customer);
        final TextInputEditText txtDate = promptsView.findViewById(R.id.txt_add_payment_date);
        final TextInputEditText txtMode = promptsView.findViewById(R.id.txt_add_payment_mode);
        final EditText edtAmount = promptsView.findViewById(R.id.edt_add_payment_amount);
        final EditText edtRefrence = promptsView.findViewById(R.id.edt_add_payment_refrence);
        Button btnCancel = promptsView.findViewById(R.id.btn_cancel);
        Button btnAdd = promptsView.findViewById(R.id.btn_add);

        txtCustomer.setVisibility(View.GONE);

        edtAmount.setText(pending_amount);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                pick_Date(txtDate);
            }
        });

        txtMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                PopupMenu popup = new PopupMenu(context, txtMode);
                popup.getMenuInflater().inflate(R.menu.menu_add_payment, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Utils.playClickSound(context);
                        int id = item.getItemId();
                        txtMode.setText(item.getTitle().toString());
                        if (id == R.id.payment_cash)
                            txtMode.setTag("1");
                        else if (id == R.id.payment_creditcard)
                            txtMode.setTag("2");
                        else if (id == R.id.payment_online)
                            txtMode.setTag("3");
                        else if (id == R.id.payment_cheque)
                            txtMode.setTag("4");
                        else if (id == R.id.payment_other)
                            txtMode.setTag("5");

                        return true;
                    }
                });

                popup.show();
            }
        });

        final ImageView img_close = (ImageView) promptsView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);

                if (txtDate.getText().toString().trim().isEmpty()) {
                    new CToast(context).simpleToast("Select Payment Date", Toast.LENGTH_SHORT)
                            .setGravityCenter()
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                    return;
                } else if (edtAmount.getText().toString().trim().isEmpty()) {
                    edtAmount.setError("Enter Amount");
                    edtAmount.requestFocus();
                    return;
                } else if (txtMode.getText().toString().trim().isEmpty()) {
                    new CToast(context).simpleToast("Select Payment Mode", Toast.LENGTH_SHORT)
                            .setGravityCenter()
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                    return;
                } else {
                    if (Utils.isOnline(context)) {
                        addPaymentApi(txtMode.getTag().toString(), edtAmount.getText().toString(), invoice_id, edtRefrence.getText().toString().trim(), txtDate.getTag().toString());
                    } else {
                        new CToast(context).simpleToast(context.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void addPaymentApi(String paymentMode, String amount, String invoice_id, String reference, String date) {
        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            sharedPreferences = SharePref.getSharePref(context);
            Call<CommonModel> call = webApi.addLumpSumPayment(context.getString(R.string.api_key), paymentMode, amount, invoice_id, reference, date
                    , sharedPreferences.getString(SharePref.USERID, ""));
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();
                            alertDialog.dismiss();
                            //invoice_listFragment.getInvoiceFirst();

                        }else {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    } else {
                        new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    private void pick_Date(final TextView txt) {

        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_datepick, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final DatePicker datePicker = promptsView.findViewById(R.id.datePicker);

        final Button btn_cancel = promptsView.findViewById(R.id.btn_cancel);
        final Button btn_ok = promptsView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                String dmonth = String.valueOf(datePicker.getMonth() + 1);
                String dday = String.valueOf(datePicker.getDayOfMonth());
                String dyear = String.valueOf(datePicker.getYear());
                alertDialog.dismiss();
                if (dmonth.length() == 1) {
                    dmonth = "0" + dmonth;
                }
                if (dday.length() == 1) {
                    dday = "0" + dday;
                }
                String selectedDate = dyear + "-" + dmonth + "-" + dday;
                txt.setText(dday + "/" + dmonth + "/" + dyear);
                txt.setTag(selectedDate);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
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

    public void updateList(ArrayList<TodayInvoiceListingModel.Result> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}
