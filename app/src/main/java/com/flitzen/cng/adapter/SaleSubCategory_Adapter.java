package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.SubCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class SaleSubCategory_Adapter extends RecyclerView.Adapter<SaleSubCategory_Adapter.ViewHolder>{

    List<SaleSubCat_Items> itemsList = new ArrayList<>();
    Context context;
    SaleCategory_Adapter.OnItemClickListener mItemClickListener;

    public SaleSubCategory_Adapter(Context context, List<SaleSubCat_Items> itemses) {
        this.context = context;
        this.itemsList = itemses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.salesubcategory_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtFirstChar.setText(itemsList.get(position).getcName().substring(0,1));
        holder.txtName.setText(itemsList.get(position).getcName().substring(1));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView mainView;
        TextView txtName,txtFirstChar;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = (CardView) itemView.findViewById(R.id.view_salesubcat_main);
            txtName = (TextView) itemView.findViewById(R.id.txt_salesubcat_a_name);
            txtFirstChar = (TextView) itemView.findViewById(R.id.txt_salepro_first_char);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final SaleCategory_Adapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {

        void onItemClick(int position);
    }

}
