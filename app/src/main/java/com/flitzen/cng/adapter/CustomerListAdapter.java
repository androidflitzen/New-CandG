package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerListModel;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder>{

    ArrayList<CustomerListModel.Result> customerList=new ArrayList<>();
    Context context;
    private ItemClickListener clickListener;

    public CustomerListAdapter(ArrayList<CustomerListModel.Result> customerList, Context context) {
        this.customerList=customerList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.adapter_customer_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txt_c_company_name,txt_c_phone_no;
        public LinearLayout main_view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_c_company_name =  itemView.findViewById(R.id.txt_c_company_name);
            this.txt_c_phone_no =  itemView.findViewById(R.id.txt_c_phone_no);
            this.main_view = itemView.findViewById(R.id.main_view);
            this.main_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClickCustomer(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickCustomer(View view, int position);
    }
}
