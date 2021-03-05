package com.flitzen.cng.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.Items.OnSaleProducts_Items;
import com.flitzen.cng.R;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OnSaleProducts_Adapter extends RecyclerView.Adapter<OnSaleProducts_Adapter.ViewHolder> {

    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    DecimalFormat formatterQty = new DecimalFormat(Helper.AMOUNT_FORMATE);
    boolean isPress = false;
    List<OnSaleProducts_Items> itemsList = new ArrayList<>();
    Context context;
    OnItemClickListner onItemClickListner;

    public OnSaleProducts_Adapter(Context context, List<OnSaleProducts_Items> itemses) {
        this.context = context;
        this.itemsList = itemses;
        formatter.setMinimumFractionDigits(2);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.onsale_products_adapter, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(itemsList.get(position).getpName());
        holder.txtQty.setText(Html.fromHtml("<b>" + formatterQty.format(itemsList.get(position).getpQty()) + "</b>"));
        holder.txtPrice.setText(context.getResources().getString(R.string.pound) + formatter.format(itemsList.get(position).getpPrice()));

        if (itemsList.get(position).getCatName() == null) {
            holder.txtRoots.setVisibility(View.GONE);
        } else {
            holder.txtRoots.setVisibility(View.VISIBLE);
            holder.txtRoots.setText(itemsList.get(position).getCatName() + " > " + itemsList.get(position).getSubcatName());
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                changeProductDetails(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRoots, txtName, txtQty, txtPrice;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_onsale_a_main);
            txtRoots =  itemView.findViewById(R.id.txt_onsale_a_roots);
            txtName =  itemView.findViewById(R.id.txt_onsale_a_name);
            txtQty =  itemView.findViewById(R.id.txt_onsale_a_qty);
            txtPrice =  itemView.findViewById(R.id.txt_onsale_a_price);
        }
    }

    public void remove(int position) {
        itemsList.remove(position);
        notifyDataSetChanged();
        onItemClickListner.updateTotal();
    }

    public void changeProductDetails(final int position) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_on_sale_product_change, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        TextView txtTitle =  promptsView.findViewById(R.id.txt_on_sale_product_title);
        TextView txtSubCatName =  promptsView.findViewById(R.id.txt_on_sale_sub_cat_title);
        final TextView txtEffectivePrice =  promptsView.findViewById(R.id.txt_on_sale_product_effectiveprice);
        Button btnCancel =  promptsView.findViewById(R.id.btn_on_sale_product_cancel);
        Button btnChange =  promptsView.findViewById(R.id.btn_on_sale_product_change);
        final TextInputEditText inputPrice =  promptsView.findViewById(R.id.input_on_sale_product_price);
        final TextInputEditText inputDiscount =  promptsView.findViewById(R.id.input_on_sale_product_disocount);
        final TextInputEditText inputQty =  promptsView.findViewById(R.id.input_on_sale_product_qty);

        txtTitle.setText(itemsList.get(position).getpName());
        txtSubCatName.setText(itemsList.get(position).getSubcatName());
        final CardView viewMinus =  promptsView.findViewById(R.id.viewMinus);
        final CardView viewPlus =  promptsView.findViewById(R.id.viewPlus);
        viewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                if (itemsList.get(position).getpQty()>1){
                    itemsList.get(position).setpQty(itemsList.get(position).getpQty()-1);
                    inputQty.setText(formatterQty.format(itemsList.get(position).getpQty()));
                }
            }
        });
        viewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                    itemsList.get(position).setpQty(itemsList.get(position).getpQty()+1);
                    inputQty.setText(formatterQty.format(itemsList.get(position).getpQty()));
            }
        });

        inputPrice.setText(formatter.format(itemsList.get(position).getpPrice()));
        inputQty.setText(formatterQty.format(itemsList.get(position).getpQty()));
        inputDiscount.setText(formatterQty.format(itemsList.get(position).getDiscount()));

        double discount = ((inputDiscount.getText().toString().trim().isEmpty()) ? 0 : itemsList.get(position).getDiscount());
        double price = itemsList.get(position).getpPrice();
        double qty = itemsList.get(position).getpQty();
        double effectivePrice = (price * qty) / 100 * discount;
        txtEffectivePrice.setText(formatter.format(((price * qty) - effectivePrice)));

        inputDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputPrice.getText().toString().trim().isEmpty()) {
                    inputPrice.setError("Enter Price");
                    return;
                }
                if (inputQty.getText().toString().trim().isEmpty()) {
                    inputQty.setError("Enter Qty");
                    return;
                }

                if (s.toString().isEmpty())
                    return;

                double discount = ((inputDiscount.getText().toString().trim().isEmpty()) ? 0 : Double.parseDouble(inputDiscount.getText().toString().trim().replace(",", "")));
                double price = Double.parseDouble(inputPrice.getText().toString().trim().replace(",", ""));
                double qty = Double.parseDouble(inputQty.getText().toString().trim().replace(",", ""));
                double effectivePrice = (price * qty) / 100 * discount;
                txtEffectivePrice.setText(formatter.format(((price * qty) - effectivePrice)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                if (inputPrice.getText().toString().trim().isEmpty()) {
                    inputPrice.setError("Enter Price");
                    return;
                }
                if (inputQty.getText().toString().trim().isEmpty()) {
                    inputQty.setError("Enter Qty");
                    return;
                }

                double discount = ((inputDiscount.getText().toString().trim().isEmpty()) ? 0 : Double.parseDouble(inputDiscount.getText().toString().trim()));
                OnSaleProducts_Items item = itemsList.get(position);
                item.setpPrice(Double.parseDouble(inputPrice.getText().toString().trim().replace(",", "")));
                item.setpQty(Double.parseDouble(inputQty.getText().toString().trim().replace(",", "")));
                item.setDiscount(discount);
                notifyItemChanged(position);
                alertDialog.dismiss();
                onItemClickListner.updateTotal();
            }
        });
        alertDialog.show();
    }

    public void SetOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner {
        public void updateTotal();
    }
}
