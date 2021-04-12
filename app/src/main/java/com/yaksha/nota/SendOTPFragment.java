package com.yaksha.nota;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import com.yaksha.nota.app.App;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.util.CustomRequest;
import com.yaksha.nota.util.Helper;

public class SendOTPFragment extends Fragment implements Constants {

    private ProgressDialog pDialog;

    Button mContinueBtn;
    EditText mMobile;
    String mobile;

    private Boolean loading = false;

    public SendOTPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sendotp, container, false);

        if (loading) {

            showpDialog();
        }

        mMobile = (EditText) rootView.findViewById(R.id.PasswordRecoveryEmail);

        mContinueBtn = (Button) rootView.findViewById(R.id.PasswordRecoveryBtn);

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile = mMobile.getText().toString();

                if (!App.getInstance().isConnected()) {

                    Toast.makeText(getActivity(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();

                } else {

                    Helper helper = new Helper();

                    if (helper.isValidPhone(mobile)) {

                        sendOTP(mobile);

                    } else {

                        Toast.makeText(getActivity(), getText(R.string.error_email), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void sendOTP(String username) {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_SIGNIN_OTP, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject authObj = response.getJSONObject("access_data");
                            if (!authObj.getBoolean("error")) {
                                String accID = authObj.getString("accountId");
                                Intent intent = new Intent(getActivity(), OtpVerifyActivity.class);
                                intent.putExtra("accID",accID);
                                intent.putExtra("phoneNo",username);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     /*   if (App.getInstance().authorize(response)) {


                        } else {

                            Toast.makeText(getActivity(), getString(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                        }*/

                        loading = false;

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }
    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}