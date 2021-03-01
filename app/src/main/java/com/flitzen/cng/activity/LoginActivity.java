package com.flitzen.cng.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flitzen.cng.CandG;
import com.flitzen.cng.R;
import com.flitzen.cng.model.LoginResponseModel;
import com.flitzen.cng.service.AllProductDataService;
import com.flitzen.cng.utils.CToast;
import com.flitzen.cng.utils.SharePref;
import com.flitzen.cng.utils.Utils;
import com.flitzen.cng.utils.WebApi;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_login_userid)
    TextInputEditText edtUserId;
    @BindView(R.id.edt_login_password)
    TextInputEditText edtPassword;
    @BindView(R.id.view_login_btn)
    CardView viewLogin;
    @BindView(R.id.prd_login)
    ProgressBar prd;
    @BindView(R.id.txtLogin)
    TextView txtLogin;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath(getResources().getString(R.string.font_regular))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        viewLogin.setOnClickListener(this);

        edtPassword.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
                            doLogin();
                            return true;
                        }
                        return false;
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_login_btn:
                Utils.playClickSound(getApplicationContext());
                doLogin();
                break;
        }
    }

    public void doLogin() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(LoginActivity.this, R.raw.please_enter_your_login_details_here);
        if (edtUserId.getText().toString().trim().isEmpty()) {
            edtUserId.setError("Enter UserId");
            edtUserId.requestFocus();
            mediaPlayer.start();
            return;
        } else if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError("Enter Password");
            edtPassword.requestFocus();
            mediaPlayer.start();
            return;
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(LoginActivity.this, R.raw.successfully_logged_in);
            if (Utils.isOnline(LoginActivity.this)) {
                getLoginRetrofit();
            } else {
                new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                        .setBackgroundColor(R.color.msg_fail)
                        .show();
            }
        }
    }

    public void getLoginRetrofit() {

        try {
            WebApi webApi = CandG.getClient().create(WebApi.class);
            txtLogin.setVisibility(View.INVISIBLE);
            prd.setVisibility(View.VISIBLE);

            Call<LoginResponseModel> call = webApi.login(getResources().getString(R.string.api_key),edtUserId.getText().toString(), edtPassword.getText().toString());
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, retrofit2.Response<LoginResponseModel> response) {
                    prd.setVisibility(View.INVISIBLE);
                    txtLogin.setVisibility(View.VISIBLE);
                    if(response.isSuccessful()){
                        if (response.body().getStatus() == 1) {
                            SharedPreferences sharedPreferences = SharePref.getSharePref(LoginActivity.this);
                            //start service
                            Intent serviceIntent = new Intent(LoginActivity.this, AllProductDataService.class);
                            serviceIntent.putExtra("user_id",sharedPreferences.getString(SharePref.USERID, ""));
                            startService(serviceIntent);

                            mediaPlayer.start();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(SharePref.LOGIN_STATUS, true);
                            editor.putString(SharePref.USERID, response.body().getData().getId());
                            editor.putString(SharePref.NAME, response.body().getData().getName());
                            editor.putString(SharePref.EMAIL, edtUserId.getText().toString());
                            editor.putString(SharePref.PASSWORD, edtPassword.getText().toString());
                            editor.commit();

                            CandG.userId = response.body().getData().getId();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new CToast(getApplicationContext()).simpleToast(response.body().getMessage(), Toast.LENGTH_SHORT)
                                    .setBackgroundColor(R.color.msg_fail)
                                    .show();
                        }
                    }else {
                        new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                                .setBackgroundColor(R.color.msg_fail)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    prd.setVisibility(View.INVISIBLE);
                    txtLogin.setVisibility(View.VISIBLE);
                    txtLogin.setText("TRY AGAIN");

                    new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                            .setBackgroundColor(R.color.msg_fail)
                            .show();
                }
            });
        }catch (Exception e){
            new CToast(getApplicationContext()).simpleToast(getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT)
                    .setBackgroundColor(R.color.msg_fail)
                    .show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}