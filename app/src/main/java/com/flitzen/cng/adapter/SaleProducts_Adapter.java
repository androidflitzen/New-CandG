package com.flitzen.cng.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.Items.SaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.model.AllProductDataModel;
import com.flitzen.cng.model.ProductModel;
import com.flitzen.cng.utils.SharePref;

import java.util.ArrayList;
import java.util.List;

public class SaleProducts_Adapter extends RecyclerView.Adapter<SaleProducts_Adapter.ViewHolder> {

    List<SaleProducts_Items> itemsList = new ArrayList<>();
    Context context;
    //ArrayList<OnSaleProducts_Items> item_sale;
    SharedPreferences sharedPreferences;

    OnItemClickListner onItemClickListner;

    public SaleProducts_Adapter(Context context, List<SaleProducts_Items> itemses) {
        this.context = context;
        this.itemsList = itemses;
        sharedPreferences = SharePref.getSharePref(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saleproducts_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            holder.txtFirstChar.setText(itemsList.get(position).getpName().substring(0,1));
            holder.txtName.setText(itemsList.get(position).getpName().substring(1));


            holder.mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListner!=null){
                        onItemClickListner.onItemClick(position);
                    }

                }
            });

            //TODO in future
            /*if (itemsList.get(position).isSelected())
                holder.mainView.setCardBackgroundColor(context.getResources().getColor(R.color.product_in_sale));
            else
                holder.mainView.setCardBackgroundColor(Color.WHITE);*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return itemsList != null ? itemsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView mainView;
        TextView txtName,txtFirstChar;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView =  itemView.findViewById(R.id.view_salepro_main);
            txtName =  itemView.findViewById(R.id.txt_salepro_a_name);
            txtFirstChar =  itemView.findViewById(R.id.txt_salepro_first_char);
        }
    }

    public void SetOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner {
        public void onItemClick(int position);
    }

}
