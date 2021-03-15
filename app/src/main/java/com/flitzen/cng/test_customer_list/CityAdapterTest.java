package com.flitzen.cng.test_customer_list;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CreditNoteListAdapter;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

public class CityAdapterTest extends RecyclerView.Adapter {

    Context context;
    List<CustomerModelTest> data;
    GridLayoutManager gridLayoutManager;

  /*  public CityAdapter(Context context) {
        super(context);
    }*/

    public CityAdapterTest(Context context, List<CustomerModelTest> data, GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager=gridLayoutManager;
        this.data=data;
        this.context=context;
        System.out.println("===========data   "+data.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        // check here the viewType and return RecyclerView.ViewHolder based on view type
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_list, parent, false);
            return new CityHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_pinned_header, parent, false);
            return new PinnedHolder(view);
        }else {
            return  null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);
        // First check here the View Type
        // than set data based on View Type to your recyclerview item
        if (itemType == 0) {
            ((CityHolder) holder).txt_c_company_name.setText(data.get(position).getName());
            ((CityHolder) holder).txt_c_phone_no.setText(data.get(position).getPhone_no());
        } else if (itemType == 1) {
            String letter = data.get(position).getName().substring(0, 1);
            ((PinnedHolder) holder).city_tip.setText(letter.toUpperCase());
        }

    }

    @Override
    public int getItemViewType(int position) {
        // based on you list you will return the ViewType
        if (Integer.parseInt(data.get(position).getType()) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < data.size(); i++){
            if(data.get(i).getType().equalsIgnoreCase("1") && data.get(i).getName().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    class CityHolder extends RecyclerView.ViewHolder {

        public TextView txt_c_company_name,txt_c_phone_no;
        public LinearLayout main_view;
        public CityHolder(View itemView) {
            super(itemView);
            this.txt_c_company_name =  itemView.findViewById(R.id.txt_c_company_name);
            this.txt_c_phone_no =  itemView.findViewById(R.id.txt_c_phone_no);
            this.main_view = itemView.findViewById(R.id.main_view);
        }
    }


    class PinnedHolder extends RecyclerView.ViewHolder {

        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = view.findViewById(R.id.city_tip);
        }
    }
}
