package com.flitzen.cng.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.TodayInvoiceListingModel;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Utils;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceTodayListAdapter extends RecyclerView.Adapter<InvoiceTodayListAdapter.ViewHolder> {

    Context context;
    ArrayList<TodayInvoiceListingModel.Result> arrayList;
    DecimalFormat numberFotmate = new DecimalFormat(Helper.AMOUNT_FORMATE);

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
        holder.txtNo.setText("CG-"+arrayList.get(position).getInvoiceId());
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
        } else if (arrayList.get(position).getSalesType().equals("Cash Sale")) {
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
        CardView mainView, txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice =  itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtNo =  itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus = itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer =  itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate =  itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime =  itemView.findViewById(R.id.txt_invoicelist_a_time);
            txtSubTotal =  itemView.findViewById(R.id.txt_invoicelist_a_c_sub_total);
            txtVatAmount =  itemView.findViewById(R.id.txt_invoicelist_a_c_vat_amount);
            txtPaidAmount =  itemView.findViewById(R.id.txt_invoicelist_a_paid);
            txtDueAmount =  itemView.findViewById(R.id.txt_invoicelist_a_due);
            txtPONo =  itemView.findViewById(R.id.txt_invoicelist_a_po_no);
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

        alertDialog.show();
    }

}
