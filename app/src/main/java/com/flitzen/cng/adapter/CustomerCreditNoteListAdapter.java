package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CustomerCreditNoteListModel;
import com.flitzen.cng.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CustomerCreditNoteListAdapter extends RecyclerView.Adapter<CustomerCreditNoteListAdapter.ViewHolder>{

    Context context;
    List<CustomerCreditNoteListModel.Data> arrayList;

    public CustomerCreditNoteListAdapter(Context context, ArrayList<CustomerCreditNoteListModel.Data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_creditnote, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerCreditNoteListModel.Data item = arrayList.get(position);

        holder.txtCnNo.setText("CN-"+item.getCreaditNoteId());
        holder.txt_po_no.setText(item.getPurchaseNo());
        holder.txtDate.setText(arrayList.get(position).getCreaditNoteDate());
        holder.txtTime.setText(arrayList.get(position).getCreaditNoteTime());
        holder.txtSalesPer.setText(item.getSalesPersonName());
        holder.txtAmount.setText(context.getResources().getString(R.string.pound) + " " + item.getFinalTotal());
        holder.txtSubTotal.setText(context.getResources().getString(R.string.pound) + " " + item.getTotalAmount());
        holder.txtVatAmount.setText(context.getResources().getString(R.string.pound) + " " + item.getVatAmount());

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(context);
                showMoreOption(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mainView;
        TextView txtCnNo, txt_po_no,txtDate, txtTime, txtSalesPer, txtAmount, txtSubTotal, txtVatAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_cnlist_a_main);
            txtCnNo =  itemView.findViewById(R.id.txt_cnlist_a_no);
            txt_po_no =  itemView.findViewById(R.id.txt_po_no);
            txtDate =  itemView.findViewById(R.id.txt_cnlist_a_date);
            txtSalesPer =  itemView.findViewById(R.id.txt_cnlist_a_sales_person);
            txtAmount =  itemView.findViewById(R.id.txt_cnlist_a_amount);
            txtSubTotal =  itemView.findViewById(R.id.txt_cnlist_a_sub_total);
            txtVatAmount =  itemView.findViewById(R.id.txt_cnlist_a_vat_amount);
            txtTime =  itemView.findViewById(R.id.txt_cnlist_a_time);

        }
    }

    public void updateList(ArrayList<CustomerCreditNoteListModel.Data> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void showMoreOption(final int position) {
        LayoutInflater localView = LayoutInflater.from(context);
        View promptsView = localView.inflate(R.layout.dialog_credit_note_list_option, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        View viewPdf = promptsView.findViewById(R.id.view_inv_dialog_view_pdf);
        View viewSendMail = promptsView.findViewById(R.id.view_inv_dialog_send_mail);

        alertDialog.show();
    }
}
