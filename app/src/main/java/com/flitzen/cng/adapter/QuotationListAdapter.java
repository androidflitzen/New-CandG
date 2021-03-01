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
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuotationListAdapter extends RecyclerView.Adapter<QuotationListAdapter.ViewHolder>
{

    ArrayList<QuotationListingModel.Result> arrayList;
    Context context;
    String gbp;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);

    public QuotationListAdapter(Context context, ArrayList<QuotationListingModel.Result> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        gbp = context.getResources().getString(R.string.pound);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_quatation_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtPrice.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getFinalTotal())));
        holder.txtSubTotal.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getTotalAmount())));
        holder.txtVatAmount.setText(gbp + formatter.format(Double.valueOf(arrayList.get(position).getVatAmount())));
        holder.txtNo.setText("QT-"+arrayList.get(position).getQuotationId());
        holder.txtCustomer.setText(arrayList.get(position).getQuotationTo());
        holder.txtDate.setText(arrayList.get(position).getQuotationDate());
        holder.txtTime.setText(arrayList.get(position).getQuotationTime());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                showMoreOption(position);
            }
        });

        if (arrayList.get(position).getConvertedStatus().equals("1")) {
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtStatus, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount;
        CardView mainView;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtSubTotal = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_sub_total);
            txtVatAmount = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_vat_amount);
            txtNo = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime = (TextView) itemView.findViewById(R.id.txt_invoicelist_a_time);

        }
    }

    public void showMoreOption(final int position) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_quotation_list_option, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        View viewPdf = promptsView.findViewById(R.id.view_quo_dialog_view_pdf);
        View viewDelete = promptsView.findViewById(R.id.view_quo_dialog_add_delete);
        View viewConvert = promptsView.findViewById(R.id.view_inv_dialog_convert_invoice);
        View viewSendMail = promptsView.findViewById(R.id.view_quo_dialog_send_mail);
        View ll_del_edit = promptsView.findViewById(R.id.ll_del_edit);

        alertDialog.show();
    }

}
