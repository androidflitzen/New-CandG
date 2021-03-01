package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerPaymentModel;

import java.util.ArrayList;

public class CustomerPaymentStatusAdapter extends RecyclerView.Adapter<CustomerPaymentStatusAdapter.ViewHolder>{

    ArrayList<CustomerPaymentModel.Payment> arrayList;
    Context context;

    public CustomerPaymentStatusAdapter(Context context, ArrayList<CustomerPaymentModel.Payment> arrayList) {
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtype, txtDate, txtNote, txtMode, txtCrDr;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_payment_amount);
            txtype = (TextView) itemView.findViewById(R.id.txt_payment_type);
            txtDate = (TextView) itemView.findViewById(R.id.txt_payment_date);
            txtNote = (TextView) itemView.findViewById(R.id.txt_payment_note);
            txtMode = (TextView) itemView.findViewById(R.id.txt_payment_mode);
        }
    }
}
