package com.flitzen.cng.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.Quotation_To_Invoice;
import com.flitzen.cng.model.CommonModel;
import com.flitzen.cng.model.CustomerQuotationListModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Permission;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class CustomerQuotationListAdapter extends RecyclerView.Adapter<CustomerQuotationListAdapter.ViewHolder> {

    public Context context;
    private ArrayList<CustomerQuotationListModel.Data> arrayList;
    private String gbp;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    ProgressDialog prd;

    public CustomerQuotationListAdapter(Context context, ArrayList<CustomerQuotationListModel.Data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        gbp = context.getResources().getString(R.string.pound);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_quatation_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerQuotationListAdapter.ViewHolder holder, int position) {
        holder.txtPrice.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getFinalTotal())));
        holder.txtSubTotal.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getTotalAmount())));
        holder.txtVatAmount.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getVatAmount())));
        holder.txtNo.setText("QT-" + arrayList.get(position).getQuotationId());
        holder.txtCustomer.setText(arrayList.get(position).getQuotationTo());
        holder.txtDate.setText(arrayList.get(position).getQuotationDate());
        holder.txtTime.setText(arrayList.get(position).getQuotationTime());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                showMoreOption(position);
            }
        });


        if (arrayList.get(position).getConvertedStatus().equals("1")) {
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount;
        CardView mainView;
        LinearLayout txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice =  itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtSubTotal =  itemView.findViewById(R.id.txt_invoicelist_a_sub_total);
            txtVatAmount =  itemView.findViewById(R.id.txt_invoicelist_a_vat_amount);
            txtNo =  itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus =  itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer =  itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate =  itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime =  itemView.findViewById(R.id.txt_invoicelist_a_time);

        }
    }

    public void updateList(ArrayList<CustomerQuotationListModel.Data> arrayList){
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

    public void showMoreOption(final int position) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_quotation_list_option, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        View viewPdf = promptsView.findViewById(R.id.view_quo_dialog_view_pdf);
        View viewDelete = promptsView.findViewById(R.id.view_quo_dialog_add_delete);
        View viewConvert = promptsView.findViewById(R.id.view_inv_dialog_convert_invoice);
        View viewSendMail = promptsView.findViewById(R.id.view_quo_dialog_send_mail);
        View ll_del_edit = promptsView.findViewById(R.id.ll_del_edit);

        //TODO in future
       /* if (!arrayList.get(position).getQuotatoin_to().equals("-")) {
            name = arrayList.get(position).getQuotatoin_to();
        }*/

        //getEmail(position);

        if (arrayList.get(position).getConvertedStatus().equals("1")) {
            ll_del_edit.setVisibility(View.GONE);
        } else {
            ll_del_edit.setVisibility(View.VISIBLE);
        }


        viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO in future

                Utils.playClickSound(context);
            }
        });

        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();

                new AlertDialog.Builder(context)
                        .setTitle("Are you sure want to delete quotation ?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
                                showPRD();
                                Utils.playClickSound(context);
                                quotationDelete(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.playClickSound(context);
                            }
                        }).show();
            }
        });

        viewConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
                Intent intent = new Intent(context, Quotation_To_Invoice.class);
                intent.putExtra("q_id", arrayList.get(position).getQuotationId());
                context.startActivity(intent);
            }
        });

        viewSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO in future
            }
        });

        alertDialog.show();
    }

    private void quotationDelete(int position) {
        try {
            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CommonModel> call = webApi.deleteQuotation(context.getResources().getString(R.string.api_key), arrayList.get(position).getQuotationId());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, retrofit2.Response<CommonModel> response) {
                    hidePRD();
                    if(response.isSuccessful()){
                        if (response.body().getStatus() == 1) {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();

                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList.size());

                        } else {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hidePRD();
                    new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            hidePRD();
            new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    public void showPRD() {
        if (prd == null) {
            prd = new ProgressDialog(context);
            prd.setMessage("Please wait...");
            prd.setCancelable(false);
        } else {
            if (!prd.isShowing()) {
                prd.show();
            }
        }
    }

    public void hidePRD() {
        if (prd != null && prd.isShowing()) {
            prd.dismiss();
        }
    }
}
