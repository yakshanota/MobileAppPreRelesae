package com.yaksha.nota;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import in.androidhunt.otp.AutoDetectOTP;
import com.yaksha.nota.app.App;
import com.yaksha.nota.common.ActivityBase;
import com.yaksha.nota.util.CustomRequest;

public class OtpVerifyActivity   extends ActivityBase {

    Toolbar mToolbar;
    Fragment fragment;

    private String otpnN0;
    TextView timer;
    AutoDetectOTP autoDetectOTP;
    private OtpTextView otpTextView;
    private String accID = "";
    CountDownTimer countDownTimer=   new CountDownTimer(180000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText(millisecondsToTime(millisUntilFinished));
        }
        @Override
        public void onFinish() {
            timer.setText("");
        }
    };
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(otpnN0!=null&&otpnN0.equals("1234")){
                otpTextView.showSuccess();
                countDownTimer.cancel();
            }
            else {
                otpTextView.showError();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        autoDetectOTP=new AutoDetectOTP(this);

        TextView phoneview=findViewById(R.id.phone_);
        timer=  findViewById(R.id.timer);
        if(getIntent().hasExtra("accID")) {
            accID = getIntent().getStringExtra("accID");
        }

        String no=getIntent().getStringExtra("phoneNo");
        if(no!=null){
            phoneview.append("+91 "+no);
        }
        otpTextView = findViewById(R.id.otp_view);
        otpTextView.requestFocusOTP();
        otpTextView.setOtpListener(new OTPListener() {;

            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                otpnN0=otp;
                Toast.makeText(OtpVerifyActivity.this,"The OTP is " + otp, Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable,100);

            }
        });
        countDownTimer.start();
        autoDetectOTP.startSmsRetriver(new AutoDetectOTP.SmsCallback() {
            @Override
            public void connectionfailed() {
                Toast.makeText(OtpVerifyActivity.this,"Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionSuccess(Void aVoid) {
                Toast.makeText(OtpVerifyActivity.this,"Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void smsCallback(String sms) {
                if(sms.contains(":") && sms.contains(".")) {
                    String otp = sms.substring( sms.indexOf(":")+1 , sms.indexOf(".") ).trim();
                    otpTextView.setOTP(otp);
                    Toast.makeText(OtpVerifyActivity.this,"The OTP is " + otp, Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.verifyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_otp = otpTextView.getOTP();
                otpCheck(str_otp);

               /* ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", AutoDetectOTP.getHashCode(OtpVerifyActivity.this));
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(OtpVerifyActivity.this,AutoDetectOTP.getHashCode(OtpVerifyActivity.this), Toast.LENGTH_SHORT).show();*/
            }

        });

    }

    private void otpCheck(String otpnNo) {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_SIGNIN_OTP_CHECK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (App.getInstance().authorize(response)) {

                            if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                App.getInstance().updateGeoLocation();

                                //sendOTP(username);

                                Intent intent = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                if (App.getInstance().getState() == ACCOUNT_STATE_BLOCKED) {

                                    App.getInstance().logout();
                                    Toast.makeText(OtpVerifyActivity.this, getText(R.string.msg_account_blocked), Toast.LENGTH_SHORT).show();

                                } else {

                                    App.getInstance().updateGeoLocation();
                                    //sendOTP(username);

                                    Intent intent = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }

                        } else {

                            Toast.makeText(getApplicationContext(), getString(R.string.error_signin), Toast.LENGTH_SHORT).show();
                        }


                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", accID);
                params.put("otp", otpnNo);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public void onPause() {

        super.onPause();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        View focusedView = getCurrentFocus();

        if (focusedView != null) {

            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed(){

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
        autoDetectOTP.stopSmsReciever();
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
    }
    private String millisecondsToTime(long milliseconds) {

        return "Time remaining " + String.format("%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
