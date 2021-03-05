 package com.flitzen.cng.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flitzen.cng.R;
import com.flitzen.cng.fragment.ComingSoonFragment;
import com.flitzen.cng.fragment.CreditNote_ListFragment;
import com.flitzen.cng.fragment.CustomersFragment;
import com.flitzen.cng.fragment.Invoice_ListFragment;
import com.flitzen.cng.fragment.PaymentFragment;
import com.flitzen.cng.fragment.Quotation_ListFragment;
import com.flitzen.cng.fragment.SaleNewFragment;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.NetworkConnectionCheck;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_logout)
    ImageView img_logout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    public static int backPressStatus = 0;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        NetworkConnectionCheck.getConnectivityStatusString(HomeActivity.this);
        sharedPreferences = SharePref.getSharePref(HomeActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.transblack));
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playClickSound(HomeActivity.this);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setHederView();

        img_logout.setOnClickListener(this);

        if (!Utils.isConfigChange) {
            Utils.isConfigChange = false;
            if (getIntent() != null) {

                String val = getIntent().getStringExtra("product_price");
                if (val != null) {
                    if (val.equals("product_price")) {
                        SaleNewFragment newFragment = new SaleNewFragment();
                        Bundle args = new Bundle();
                        args.putString("product_price", val);
                        newFragment.setArguments(args);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.framelayout_home, newFragment);
                        transaction.commit();

                    }
                } else {
                    replaceFragment(new SaleNewFragment());
                }

                String type = getIntent().getStringExtra("type");

                if(type!=null){
                    if (type.equals("Invoice")) {
                        replaceFragment(new Invoice_ListFragment());
                        navigationView.getMenu().getItem(1).setChecked(true);
                        backPressStatus = 1;
                    } else {
                        replaceFragment(new SaleNewFragment());
                        navigationView.getMenu().getItem(0).setChecked(true);
                    }
                }
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Utils.playClickSound(getApplicationContext());
        if (id == R.id.nav_sale) {
            tvTitle.setText(getResources().getString(R.string.sale));
            replaceFragment(new SaleNewFragment());
            backPressStatus = 0;
        } else if (id == R.id.nav_invoice) {
            tvTitle.setText(getResources().getString(R.string.invoices));
            replaceFragment(new Invoice_ListFragment());
            backPressStatus = 1;
        } else if (id == R.id.nav_quatation) {
            tvTitle.setText(getResources().getString(R.string.quotations));
            replaceFragment(new Quotation_ListFragment());
            backPressStatus = 1;
        } else if (id == R.id.nav_cusromers) {
            tvTitle.setText(getResources().getString(R.string.customers));
            //replaceFragment(new CustomersFragment());
             replaceFragment(new ComingSoonFragment());
            backPressStatus = 1;
        } else if (id == R.id.nav_cr_notes) {
            //getSupportActionBar().setTitle(getResources().getString(R.string.credit_notes));
            tvTitle.setText(getResources().getString(R.string.credit_notes));
            replaceFragment(new CreditNote_ListFragment());
            backPressStatus = 1;
        } else if (id == R.id.nav_payment) {
            //getSupportActionBar().setTitle(R.string.transactions);
            tvTitle.setText(getResources().getString(R.string.transactions));
            //replaceFragment(new PaymentFragment());
            replaceFragment(new ComingSoonFragment());
            backPressStatus = 1;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_home, fragment)
                .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                .commit();
    }

    public void setHederView() {
        View view = navigationView.getHeaderView(0);
        TextView txtName = (TextView) view.findViewById(R.id.txt_nav_header_name);
        TextView txtEmail = (TextView) view.findViewById(R.id.txt_nav_header_email);
        txtName.setText(sharedPreferences.getString(SharePref.NAME, "User"));
        txtEmail.setText(sharedPreferences.getString(SharePref.EMAIL, "-"));
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressStatus == 1) {
                replaceFragment(new SaleNewFragment());
                backPressStatus = 0;
                navigationView.getMenu().getItem(0).setChecked(true);
            } else {
                if (doubleBackToExitPressedOnce) {
                    finish();
                }

                if (doubleBackToExitPressedOnce == false) {
                    new CToast(HomeActivity.this).simpleToast(getString(R.string.exit), Toast.LENGTH_SHORT).show();

                }

                this.doubleBackToExitPressedOnce = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.isConfigChange = true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Utils.playClickSound(getApplicationContext());
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("C&G")
                .setMessage(R.string.logout_conformation)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.playClickSound(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.playClickSound(getApplicationContext());
                    }
                }).show();
    }

   /* @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }*/
}