package com.flitzen.cng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.model.CrediNotesListModel;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreditNoteListAdapter extends RecyclerView.Adapter<CreditNoteListAdapter.ViewHolder> {

    Context context;
    ArrayList<CrediNotesListModel.Result> arrayListTemp;

    public CreditNoteListAdapter(Context context, ArrayList<CrediNotesListModel.Result> arrayListTemp) {
        this.context = context;
        this.arrayListTemp = arrayListTemp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_creditnote_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtCnNo.setText("CN-"+arrayListTemp.get(position).getCreaditNoteId());
        holder.txtDate.setText(arrayListTemp.get(position).getCreaditNoteDate() + " " + arrayListTemp.get(position).getCreaditNoteTime());
        holder.txtTime.setText(arrayListTemp.get(position).getCreaditNoteTime());
        holder.txtTo.setText(arrayListTemp.get(position).getCreditNoteTo());
        holder.txtSalesPer.setText(arrayListTemp.get(position).getSalesPersonName());
        holder.txtAmount.setText(context.getResources().getString(R.string.pound) + " " + arrayListTemp.get(position).getFinalTotal());
        holder.txtSubTotal.setText(context.getResources().getString(R.string.pound) + " " + arrayListTemp.get(position).getTotalAmount());
        holder.txtVatAmount.setText(context.getResources().getString(R.string.pound) + " " + arrayListTemp.get(position).getVatAmount());
        if (arrayListTemp.get(position).getPurchase_no() != null && !arrayListTemp.get(position).getPurchase_no().equals("")) {
            holder.txt_creditnote_a_po_no.setText(arrayListTemp.get(position).getPurchase_no());
        } else {
            holder.txt_creditnote_a_po_no.setText("-");
        }

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
        return arrayListTemp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView mainView;
        TextView txtCnNo, txtDate, txt_creditnote_a_po_no, txtTime, txtTo, txtSalesPer, txtAmount, txtSubTotal, txtVatAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_cnlist_a_main);
            txtCnNo =  itemView.findViewById(R.id.txt_cnlist_a_no);
            txtDate =  itemView.findViewById(R.id.txt_cnlist_a_date);
            txt_creditnote_a_po_no =  itemView.findViewById(R.id.txt_creditnote_a_po_no);
            txtTime =  itemView.findViewById(R.id.txt_cnlist_a_time);
            txtTo =  itemView.findViewById(R.id.txt_cnlist_a_to);
            txtSalesPer =  itemView.findViewById(R.id.txt_cnlist_a_sales_person);
            txtAmount =  itemView.findViewById(R.id.txt_cnlist_a_amount);
            txtSubTotal =  itemView.findViewById(R.id.txt_cnlist_a_sub_total);
            txtVatAmount =  itemView.findViewById(R.id.txt_cnlist_a_vat_amount);

        }
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

    public void updateList(ArrayList<CrediNotesListModel.Result> arrayList) {
        this.arrayListTemp = arrayList;
        notifyDataSetChanged();
    }

}
