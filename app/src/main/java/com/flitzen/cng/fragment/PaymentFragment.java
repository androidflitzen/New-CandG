package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.R;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.PaymentListAdapter;
import com.flitzen.cng.model.CustomerSpinnerListModel;
import com.flitzen.cng.model.PaymentListModel;
import com.flitzen.cng.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentFragment extends Fragment implements View.OnClickListener {

    View viewPayments;
    PaymentListAdapter paymentListAdapter;

    @BindView(R.id.view_payment_content)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_payment_list)
    RecyclerView recyclerView;
    @BindView(R.id.view_payment_empty)
    TextView viewEmpty;
    @BindView(R.id.fab_payment_add)
    FloatingActionButton fabAdd;

    ArrayList<PaymentListModel.Result> paymentList=new ArrayList<>();
    ArrayList<CustomerSpinnerListModel> arrayListCustomer = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPayments = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, viewPayments);

        paymentListAdapter = new PaymentListAdapter(getActivity(),paymentList,recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(paymentListAdapter);

        fabAdd.setOnClickListener(this);

        return viewPayments;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tvTitle=((HomeActivity)getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.transactions));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_payment_add:
                openAddPayment();
               /* Utils.playClickSound(getActivity());
                if (arrayListCustomer.size() == 0) {
                    getCustomer();
                } else {
                    openAddPayment();
                }*/
                break;
        }
    }

    public void openAddPayment() {
        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_add_payment, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final TextView txtCustomer = (TextView) promptsView.findViewById(R.id.txt_add_payment_customer);
        final TextView txtDate = (TextView) promptsView.findViewById(R.id.txt_add_payment_date);
        final TextView txtMode = (TextView) promptsView.findViewById(R.id.txt_add_payment_mode);
        final EditText edtAmount = (EditText) promptsView.findViewById(R.id.edt_add_payment_amount);
        final EditText edtRefrence = (EditText) promptsView.findViewById(R.id.edt_add_payment_refrence);
        Button btnCancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        Button btnAdd = (Button) promptsView.findViewById(R.id.btn_add);

        txtCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                //selectCustomer(txtCustomer);
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                pick_Date(txtDate);
            }
        });

        final ImageView img_close = (ImageView) promptsView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                alertDialog.dismiss();
            }
        });

        txtMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                PopupMenu popup = new PopupMenu(getActivity(), txtMode);
                popup.getMenuInflater().inflate(R.menu.menu_add_payment, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Utils.playClickSound(getActivity());
                        int id = item.getItemId();
                        txtMode.setText(item.getTitle().toString());
                        if (id == R.id.payment_cash)
                            txtMode.setTag("0");
                        else if (id == R.id.payment_creditcard)
                            txtMode.setTag("1");
                        else if (id == R.id.payment_online)
                            txtMode.setTag("3");
                        else if (id == R.id.payment_cheque)
                            txtMode.setTag("4");
                        else if (id == R.id.payment_other)
                            txtMode.setTag("5");

                        return true;
                    }
                });

                popup.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(getActivity());
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void pick_Date(final TextView txt) {

        LayoutInflater localView = LayoutInflater.from(getActivity());
        View promptsView = localView.inflate(R.layout.dialog_datepick, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePicker);

        final Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        final Button btn_ok = (Button) promptsView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                String dmonth = String.valueOf(datePicker.getMonth() + 1);
                String dday = String.valueOf(datePicker.getDayOfMonth());
                String dyear = String.valueOf(datePicker.getYear());
                alertDialog.dismiss();
                if (dmonth.length() == 1) {
                    dmonth = "0" + dmonth;
                }
                if (dday.length() == 1) {
                    dday = "0" + dday;
                }
                String selectedDate = dyear + "-" + dmonth + "-" + dday;
                txt.setText(dday + "/" + dmonth + "/" + dyear);
                txt.setTag(selectedDate);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.playClickSound(getActivity());
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }
}
