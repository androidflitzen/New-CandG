package com.flitzen.cng.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.cng.BuildConfig;
import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.activity.Quotation_To_Invoice;
import com.flitzen.cng.model.CommonModel;

import com.flitzen.cng.model.QuotationListingModel;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.Helper;
import com.flitzen.cng.utils.Permission;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class QuotationListAdapter extends RecyclerView.Adapter<QuotationListAdapter.ViewHolder> {

    ArrayList<QuotationListingModel.Result> arrayList;
    Activity context;
    String gbp;
    DecimalFormat formatter = new DecimalFormat(Helper.AMOUNT_FORMATE);
    ProgressDialog prd;
    int type;
    Intent gcm_rec;
    private String unique_id = "", email = "";
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_ADMIN};
    public static final int PERMISSION_CODE = 123;
    private static final int BUFFER_SIZE = 4096;

    public QuotationListAdapter(Activity context, ArrayList<QuotationListingModel.Result> arrayList, int type) {
        this.arrayList = arrayList;
        this.context = context;
        this.type = type;
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
        //holder.txtNo.setText("QT-1234");
        holder.txtNo.setText("QT-" + arrayList.get(position).getQuotationId());
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

        TextView txtPrice, txtNo, txtCustomer, txtDate, txtTime, txtSubTotal, txtVatAmount;
        CardView mainView;
        LinearLayout txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.view_invoicelist_a_main);
            txtPrice = itemView.findViewById(R.id.txt_invoicelist_a_amount);
            txtSubTotal = itemView.findViewById(R.id.txt_invoicelist_a_sub_total);
            txtVatAmount = itemView.findViewById(R.id.txt_invoicelist_a_vat_amount);
            txtNo = itemView.findViewById(R.id.txt_invoicelist_a_no);
            txtStatus = itemView.findViewById(R.id.txt_invoicelist_a_status);
            txtCustomer = itemView.findViewById(R.id.txt_invoicelist_a_c_name);
            txtDate = itemView.findViewById(R.id.txt_invoicelist_a_date);
            txtTime = itemView.findViewById(R.id.txt_invoicelist_a_time);

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

        unique_id = arrayList.get(position).getQuotationId();
        email = arrayList.get(position).getCustomerEmail();
        //TODO in future
       /* if (!arrayList.get(position).getQuotatoin_to().equals("-")) {
            name = arrayList.get(position).getQuotatoin_to();
        }*/

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

                Utils.playClickSound(context);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Permission.hasPermissions(context, permissions)) {
                        downloadFile(position, 0);
                    } else {
                        Permission.requestPermissions(context, permissions, PERMISSION_CODE);
                    }
                } else {
                    downloadFile(position, 0);
                }
                alertDialog.dismiss();
            }
        });

        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(context);
                alertDialog.dismiss();

                new AlertDialog.Builder(context)
                        .setTitle("Are you sure want to delete quotation ?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
                                Utils.playClickSound(context);
                                quotationDelete(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.playClickSound(context);
                            }
                        }).show();
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
    }

    private void quotationDelete(int position) {
        try {

            showPRD();
            WebApi webApi = CandG.getClient().create(WebApi.class);
            Call<CommonModel> call = webApi.deleteQuotation(context.getResources().getString(R.string.api_key), arrayList.get(position).getQuotationId());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, retrofit2.Response<CommonModel> response) {
                    hidePRD();
                    if(response.isSuccessful()){
                        if (response.body().getStatus() == 1) {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_success)
                                    .show();

                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList.size());

                            if (type == 0) {
                                gcm_rec = new Intent(context.getResources().getString(R.string.remove_today_quotation));
                            } else if (type == 1) {
                                gcm_rec = new Intent(context.getResources().getString(R.string.remove_week_quotation));
                            } else if (type == 2) {
                                gcm_rec = new Intent(context.getResources().getString(R.string.remove_month_quotation));
                            } else if (type == 3) {
                                gcm_rec = new Intent(context.getResources().getString(R.string.remove_year_quotation));
                            }
                            LocalBroadcastManager.getInstance(context).sendBroadcast(gcm_rec);

                        } else {
                            new CToast(context).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hidePRD();
                    new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            hidePRD();
            new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

   /* class DownloadFile extends AsyncTask<Void, Void, String> {

        String url;
        File file;
        int action;

        public DownloadFile(String url, File file, int action) {
            this.url = url;
            this.file = file;
            this.action = action;
        }

        @Override
        protected String doInBackground(Void... voids) {
            downloadFile(url, file);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hidePRD();
            if (action == 1) {
                if (file.exists()) {
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setPackage("com.google.android.apps.docs");
                    target.setDataAndType(FileProvider.getUriForFile(context,
                            context.getPackageName(),
                            file), "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                } else {
                    Toast.makeText(context, "File path is incorrect.", Toast.LENGTH_LONG).show();
                }
            } else {
                try {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quotation- " + unique_id);
                    emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
                    } else {
                        uri = Uri.fromFile(file);
                    }

                    if (uri != null) {
                        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    String desc = "Thank you for placing an enquiry at C&G. \n" +
                            "\n" +
                            "Your quote has been attached to this email. Please feel free to contact us in case you have any questions or concerns.\n" +
                            "\n" +
                            "If you wish to pay by BACS please see details below.\n" +
                            "\n" +
                            "-----------------------\n" +
                            "NatWest \n" +
                            "Account no: 66682266\n" +
                            "Sort code: 60-15-28\n" +
                            "-----------------------\n" +
                            "\n" +
                            "This quotation is only valid for 30 days.  \n" +
                            "\n" +
                            "Also, we would like you to check out our website:  https://candgheating.com/  \n" +
                            "\n" +
                            "Reg Office: 46 Mill Road\n" +
                            "Northumberland Heath \n" +
                            "Erith\n" +
                            "Kent \n" +
                            "DA8 1HN \n" +
                            "Tel: 01322338526\n" +
                            "Email: candgheating@btconnect.com\n" +
                            "Reg no: 5288707";
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, desc);
                    context.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
                } catch (Throwable t) {
                    Toast.makeText(context, "Request failed try again: " + t.toString(), Toast.LENGTH_LONG).show();
                }

                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            }

        }
    }*/

    private void downloadFile(int position, int action) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            showPRD();
            DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
            File myFile = new File(new File(Utils.getItemDir()), arrayList.get(position).getQuotationId() + df.format(new Date()) + ".pdf");

            myFile.createNewFile();
          //  new DownloadFileFromURL(myFile, action).execute("https://new.earthingcare.com/gen_quotation?quotation_id=1848");
            new DownloadFileFromURL(myFile, action).execute(arrayList.get(position).getQuotationPdfUrl());
          //  new DownloadFileFromURL(myFile, action).execute("https://newnew.candgbathrooms.com/quotation/pdf2?quotation_id=1");

        } catch (Exception e) {
            // Utils.showToast(context, "Something Wrong...", R.color.msg_fail);
            e.printStackTrace();
        }
    }

    private void openPDF(File myFile) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setPackage("com.google.android.apps.docs");
        target.setDataAndType(FileProvider.getUriForFile(context,
                context.getPackageName(),
                myFile), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, Void> {
        File myFile;
        int action;

        public DownloadFileFromURL(File myFile, int action) {
            this.myFile = myFile;
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(myFile);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Void r) {
            hidePRD();
            if (action == 0) {
              //  openPDF(myFile);
            } else if (action == 1) {
                //sendMailInvoice(myFile);
            }
        }
    }

    public void showPRD() {
        if (prd == null) {
            prd = new ProgressDialog(context);
            prd.setMessage("Please wait...");
            prd.setCancelable(false);
        } else {
            if (!prd.isShowing()) {
                prd.show();
            }
        }
    }

    public void hidePRD() {
        if (prd != null && prd.isShowing()) {
            prd.dismiss();
        }
    }

    public void updateList(ArrayList<QuotationListingModel.Result> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}
