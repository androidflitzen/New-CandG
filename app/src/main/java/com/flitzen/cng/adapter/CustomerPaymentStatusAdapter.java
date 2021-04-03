package com.flitzen.cng.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerLedgerModel;

import java.util.ArrayList;

public class CustomerPaymentStatusAdapter extends RecyclerView.Adapter<CustomerPaymentStatusAdapter.ViewHolder>{

    ArrayList<CustomerLedgerModel.Data> arrayList;
    Context context;

    public CustomerPaymentStatusAdapter(Context context, ArrayList<CustomerLedgerModel.Data> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_payment_status, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPrice.setText(context.getResources().getString(R.string.pound) + " " + arrayList.get(position).getAmount());
        holder.txtype.setText(arrayList.get(position).getPaymentType());
        holder.txtDate.setText(arrayList.get(position).getDateTime());
        holder.txtNote.setText(arrayList.get(position).getReference());

        if (!arrayList.get(position).getPaymentMode().equals("")) {
            holder.txtMode.setText(arrayList.get(position).getPaymentMode());
        }

        if (arrayList.get(position).getIsCrDr().equalsIgnoreCase("Cr")) {
            holder.txtPrice.setText(Html.fromHtml(context.getResources().getString(R.string.pound) + " " + arrayList.get(position).getAmount() + " " + colorTextview(context.getResources().getColor(R.color.msg_success), "Cr")));
        } else if (arrayList.get(position).getIsCrDr().equalsIgnoreCase("Dr")) {
            holder.txtPrice.setText(Html.fromHtml(context.getResources().getString(R.string.pound) + " " + arrayList.get(position).getAmount() + " " + colorTextview(context.getResources().getColor(R.color.colorPrimary), "Dr")));
        } else {
            holder.txtPrice.setText("-");
        }
    }

    private String colorTextview(int color, String val2) {

        return "<font color=" + color + ">" + " " + val2 + "</font>";
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtype, txtDate, txtNote, txtMode, txtCrDr;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice =  itemView.findViewById(R.id.txt_payment_amount);
            txtype =  itemView.findViewById(R.id.txt_payment_type);
            txtDate =  itemView.findViewById(R.id.txt_payment_date);
            txtNote =  itemView.findViewById(R.id.txt_payment_note);
            txtMode =  itemView.findViewById(R.id.txt_payment_mode);
        }
    }
}
