package com.flitzen.cng.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.flitzen.cng.R;
import com.flitzen.cng.adapter.InvoicePagerAdapter;
import com.flitzen.cng.fragment.CustomerCreditNoteFragment;
import com.flitzen.cng.fragment.CustomerInvoiceFragment;
import com.flitzen.cng.fragment.CustomerMontlyStatementFragment;
import com.flitzen.cng.fragment.CustomerPaymentStatusFragment;
import com.flitzen.cng.fragment.CustomerProfileFragment;
import com.flitzen.cng.fragment.CustomerQuatationFragment;
import com.flitzen.cng.fragment.QuatationFragment;
import com.flitzen.cng.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.layoutMainInvoice)
    LinearLayout layoutMainInvoice;
    @BindView(R.id.tabview_order)
    TabLayout tabView;
    @BindView(R.id.viewpager_order)
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setOffscreenPageLimit(6);

        List<Fragment> mFragments = new ArrayList<>();
        mFragments .add(new CustomerProfileFragment());
        mFragments .add(new CustomerInvoiceFragment());
        mFragments .add(new CustomerPaymentStatusFragment());
        mFragments .add(new CustomerMontlyStatementFragment());
        mFragments .add(new CustomerQuatationFragment());
        mFragments .add(new CustomerCreditNoteFragment());
        InvoicePagerAdapter mAdapter = new InvoicePagerAdapter(CustomerDetailsActivity.this, mFragments);
        viewPager.setAdapter(mAdapter);

        new TabLayoutMediator(tabView, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0)
                    tab.setText(getResources().getString(R.string.profile_detail));
                else if(position == 1)
                    tab.setText(getResources().getString(R.string.invoices));
                else if(position == 2)
                    tab.setText(getResources().getString(R.string.ledger));
                else if(position == 3)
                    tab.setText(getResources().getString(R.string.monthly_statement));
                else if(position == 4)
                    tab.setText(getResources().getString(R.string.quotations));
                else if(position == 5)
                    tab.setText(getResources().getString(R.string.credit_notes));
            }
        }).attach();

        for(int i=0; i < tabView.getTabCount(); i++) {
            View tab = ((ViewGroup) tabView.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(25, 0, 25, 0);
            tab.requestLayout();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Utils.playClickSound(getApplicationContext());
        finish();
        return true;
    }
}