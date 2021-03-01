package com.flitzen.cng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.flitzen.cng.R;
import com.flitzen.cng.activity.HomeActivity;
import com.flitzen.cng.adapter.InvoicePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Invoice_ListFragment extends Fragment {

    View viewInvoice;

    @BindView(R.id.tabview_invoice)
    TabLayout tabView;
    @BindView(R.id.viewPager_all_invoice)
    ViewPager2 viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewInvoice = inflater.inflate(R.layout.fragment_invoice_list, container, false);
        ButterKnife.bind(this, viewInvoice);

        viewPager.setOffscreenPageLimit(4);

        List<Fragment> mFragments = new ArrayList<>();
        mFragments .add(new Invoice_TodayFragment());
        mFragments .add(new Invoice_WeekFragment());
        mFragments .add(new Invoice_MonthFragment());
        mFragments .add(new Invoice_AllFragment());
        InvoicePagerAdapter mAdapter = new InvoicePagerAdapter(getActivity(), mFragments);
        viewPager.setAdapter(mAdapter);

        new TabLayoutMediator(tabView, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0)
                    tab.setText(getResources().getString(R.string.today));
                else if(position == 1)
                    tab.setText(getResources().getString(R.string.this_week));
                else if(position == 2)
                    tab.setText(getResources().getString(R.string.this_month));
                else if(position == 3)
                    tab.setText(getResources().getString(R.string.all_invoices));
            }
        }).attach();

        for(int i=0; i < tabView.getTabCount(); i++) {
            View tab = ((ViewGroup) tabView.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(25, 0, 25, 0);
            tab.requestLayout();
        }

        return viewInvoice;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tvTitle=((HomeActivity)getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.invoices));
    }
}
