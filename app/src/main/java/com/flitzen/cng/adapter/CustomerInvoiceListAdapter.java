package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerInvoiceListModel;

import java.util.ArrayList;

public class CustomerInvoiceListAdapter extends RecyclerView.Adapter<CustomerInvoiceListAdapter.ViewHolder>{

    ArrayList<CustomerInvoiceListModel.Invoice> arrayList = new ArrayList<>();
    Context context;

    public CustomerInvoiceListAdapter(Context context, ArrayList<CustomerInvoiceListModel.Invoice> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_invoice_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount, txtPaidAmount, txtDueAmount, txtPONo;
        View mainView, txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtNo = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus = itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_time);
            txtSubTotal = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_c_sub_total);
            txtVatAmount = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_c_vat_amount);
            txtPaidAmount = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_paid);
            txtDueAmount = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_due);
            txtPONo = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_po_no);
        }
    }
}
