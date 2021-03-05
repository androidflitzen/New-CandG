package com.flitzen.cng.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.R;
import com.flitzen.cng.activity.Quotation_To_Invoice;
import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuotationListAdapterTest extends RecyclerView.Adapter
{

    ArrayList<Object> arrayList;
    Context context;
    String gbp;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);

    private static final int DATE = 1;
    private static final int LIST = 2;

    public QuotationListAdapterTest(Context context, ArrayList<Object> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        gbp = context.getResources().getString(R.string.pound);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        switch (viewType) {
            case DATE:
                View layoutOne
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.date_layout_test, parent,
                                false);
                return new ViewHolderDate(layoutOne);
            case LIST:
                View layoutTwo
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_quatation_list, parent,
                                false);
                return new ViewHolderList(layoutTwo);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof String) {
            return DATE;
        } else if (arrayList.get(position) instanceof QuotationListingModel.Result) {
            return LIST;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case DATE:
                String result1= (String) arrayList.get(position);
                ((ViewHolderDate)holder).txtDateTest.setText(result1);
                break;
            case LIST:
                QuotationListingModel.Result result= (QuotationListingModel.Result) arrayList.get(position);
                ((ViewHolderList)holder).txtPrice.setText(gbp + formatter.format(Double.valueOf(result.getFinalTotal())));
                ((ViewHolderList)holder).txtSubTotal.setText(gbp + formatter.format(Double.valueOf(result.getTotalAmount())));
                ((ViewHolderList)holder).txtVatAmount.setText(gbp + formatter.format(Double.valueOf(result.getVatAmount())));
                ((ViewHolderList)holder).txtNo.setText("QT-"+result.getQuotationId());
                ((ViewHolderList)holder).txtCustomer.setText(result.getQuotationTo());
                ((ViewHolderList)holder).txtDate.setText(result.getQuotationDate());
                ((ViewHolderList)holder).txtTime.setText(result.getQuotationTime());
                ((ViewHolderList)holder).mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.playClickSound(context);
                        //showMoreOption(position);
                    }
                });

                if (result.getConvertedStatus().equals("1")) {
                    ((ViewHolderList)holder).txtStatus.setVisibility(View.VISIBLE);
                } else {
                    ((ViewHolderList)holder).txtStatus.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {

        TextView txtPrice, txtNo, txtStatus, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount;
        CardView mainView;

        public ViewHolderList(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice =  itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtSubTotal =  itemView.findViewById(R.id.txt_invoicelist_a_sub_total);
            txtVatAmount =  itemView.findViewById(R.id.txt_invoicelist_a_vat_amount);
            txtNo =  itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus =  itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer =  itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate =  itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime =  itemView.findViewById(R.id.txt_invoicelist_a_time);

        }
    }

    public class ViewHolderDate extends RecyclerView.ViewHolder {

        TextView txtDateTest;

        public ViewHolderDate(View itemView) {
            super(itemView);
            txtDateTest = itemView.findViewById(R.id.txtDateTest);
        }
    }

    /*public void showMoreOption(final int position) {
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


        //TODO in future
       *//* if (!arrayList.get(position).getQuotatoin_to().equals("-")) {
            name = arrayList.get(position).getQuotatoin_to();
        }*//*

        //getEmail(position);

        if (arrayList.get(position).getConvertedStatus().equals("1")) {
            ll_del_edit.setVisibility(View.GONE);
        } else {
            ll_del_edit.setVisibility(View.VISIBLE);
        }


        viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO in future
            }
        });

        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO in future
            }
        });

        viewConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();
                Intent intent = new Intent(context, Quotation_To_Invoice.class);
                intent.putExtra("q_id", arrayList.get(position).getQuotationId());
                context.startActivity(intent);
            }
        });

        viewSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO in future
            }
        });

        alertDialog.show();
    }*/
}
