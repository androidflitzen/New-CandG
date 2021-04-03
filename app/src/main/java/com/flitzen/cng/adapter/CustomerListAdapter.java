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
import com.flitzen.cng.model.CustomerModel;
import com.flitzen.cng.test_customer_list.CityAdapterTest;
import com.flitzen.cng.test_customer_list.CustomerModelTest;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter {

    ArrayList<CustomerModelTest> customerList = new ArrayList<>();
    Context context;
    private ItemClickListener clickListener;
    public static int PINNED_TYPE = 1;
    public static int DETAIL_TYPE = 0;

    public CustomerListAdapter(ArrayList<CustomerModelTest> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
        System.out.println("==========list   "+customerList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        // check here the viewType and return RecyclerView.ViewHolder based on view type
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_list, parent, false);
            return new DetailHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_pinned_header, parent, false);
            return new PinnedHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        if (itemType == 0) {
            ((DetailHolder) holder).txt_c_company_name.setText(customerList.get(position).getName());
            ((DetailHolder) holder).txt_c_phone_no.setText(customerList.get(position).getPhone_no());
        } else if (itemType == 1) {
            String letter = customerList.get(position).getName().substring(0, 1);
            ((PinnedHolder) holder).city_tip.setText(letter.toUpperCase());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Integer.parseInt(customerList.get(position).getType()) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public int getLetterPosition(String letter) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getType().equalsIgnoreCase("1") && customerList.get(i).getName().substring(0, 1).equalsIgnoreCase(letter)) {
                return i;
            }
        }
        return -1;
    }

    class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_c_company_name, txt_c_phone_no;
        public LinearLayout main_view;

        public DetailHolder(View itemView) {
            super(itemView);
            this.txt_c_company_name = itemView.findViewById(R.id.txt_c_company_name);
            this.txt_c_phone_no = itemView.findViewById(R.id.txt_c_phone_no);
            this.main_view = itemView.findViewById(R.id.main_view);
            this.main_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClickCustomer(view, getAdapterPosition());
        }
    }

    class PinnedHolder extends RecyclerView.ViewHolder {

        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = view.findViewById(R.id.city_tip);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickCustomer(View view, int position);
    }
}
