package com.yaksha.nota;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.fragment.app.Fragment;

import com.yaksha.nota.adapter.CustomListAdapterDialog;
import com.yaksha.nota.app.App;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.model.MelaList;
import com.yaksha.nota.util.CustomRequest;

public class CreateEventFragment extends Fragment implements Constants, CustomListAdapterDialog.PositionClickListener {

    private static final String STATE_LIST = "State Adapter Data";

    EditText edt_ename, edt_eplace, edt_edescription;
    TextView txt_emela, txt_eimage, txt_edate;
    ImageView imv_event;

    Button btn_save;

    String str_ename, str_eplace, str_edescription, str_emala, str_eimage, str_edate;
    private int arrayLength = 0;

    private ArrayList<MelaList> itemsList;
    private ArrayList<MelaList> itemsList1;

    private Dialog dialog;
    private Boolean click_status = false;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    public EventCalenderFragment newInstance(Boolean pager) {

        EventCalenderFragment myFragment = new EventCalenderFragment();

        Bundle args = new Bundle();
        args.putBoolean("pager", pager);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_events, container, false);


        edt_ename = (EditText) rootView.findViewById(R.id.username_edit);
        edt_eplace = rootView.findViewById(R.id.fullname_edit);
        edt_edescription = rootView.findViewById(R.id.edt_desc);

        txt_emela = rootView.findViewById(R.id.event_mela);
        txt_eimage = rootView.findViewById(R.id.event_image);
        txt_edate = rootView.findViewById(R.id.event_date);
        imv_event = rootView.findViewById(R.id.imv_event);

        btn_save = rootView.findViewById(R.id.button_continue);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Vaildate()) {
                    createEvent();
                }
            }
        });

        txt_emela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItems();

            }
        });

        txt_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        txt_edate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);

                datePickerDialog.show();

            }
        });

        txt_eimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemsImages();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public Boolean Vaildate() {
        str_ename = edt_ename.getText().toString();
        str_eplace = edt_eplace.getText().toString();
        str_edescription = edt_edescription.getText().toString();
        str_emala = txt_emela.getText().toString();
        str_edate = txt_edate.getText().toString();

        edt_ename.setError(null);
        edt_eplace.setError(null);
        edt_edescription.setError(null);
        txt_emela.setError(null);
        txt_edate.setError(null);

        if (str_ename.length() == 0) {
            edt_ename.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (str_eplace.length() == 0) {
            edt_eplace.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (str_edescription.length() == 0) {
            edt_edescription.setError(getString(R.string.error_field_empty));
            return false;
        }
        if (str_emala.length() == 0) {
            txt_emela.setError(getString(R.string.error_field_empty));
            return false;
        }


        if (str_edate.length() == 0) {
            txt_edate.setError(getString(R.string.error_field_empty));
            return false;
        }

        return true;
    }

    public void getItems() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_EVENTS_MELA_LIST, null,
                response -> {

                    itemsList = new ArrayList<>();
                    try {
                        if (!response.getBoolean("error")) {
                            JSONArray array = response.getJSONArray("data");
                            arrayLength = array.length();
                            if (arrayLength > 0) {

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject userObj = array.getJSONObject(i);

                                    MelaList community = new MelaList(userObj);

                                    itemsList.add(community);
                                }
                            }

                        }
                        showDialog(itemsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        // loadingComplete();

                    }

                }, error -> {

            //loadingComplete();
            Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }


    public void getItemsImages() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_EVENTS_MELA_LIST, null,
                response -> {

                    itemsList1 = new ArrayList<>();
                    try {
                        if (!response.getBoolean("error")) {
                            JSONArray array = response.getJSONArray("data");
                            arrayLength = array.length();
                            if (arrayLength > 0) {

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject userObj = array.getJSONObject(i);

                                    MelaList community = new MelaList(userObj);

                                    itemsList1.add(community);
                                }
                            }

                        }
                        showDialogImages(itemsList1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        // loadingComplete();

                    }

                }, error -> {

            //loadingComplete();
            Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void createEvent() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_EVENTS_CREATE, null,
                response -> {
                    try {
                        if (!response.getBoolean("error")) {
                            Toast.makeText(getActivity(), getString(R.string.event_sucess), Toast.LENGTH_LONG).show();
                            getActivity().onBackPressed();
                        }
                        showDialog(itemsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        // loadingComplete();

                    }

                }, error -> {

            //loadingComplete();
            Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_ID", Long.toString(App.getInstance().getId()));
                params.put("user_name", App.getInstance().getUsername());
                params.put("event_date", str_edate);
                params.put("event_mela", str_emala);
                params.put("event_name", str_ename);
                params.put("event_place", str_eplace);
                params.put("event_description", str_edescription);
                params.put("event_image", str_eimage);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    private void showDialog(ArrayList<MelaList> list) {
        click_status = false;
        dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.item_row, null);

        ListView lv = (ListView) view.findViewById(R.id.listView);

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterDialog clad = new CustomListAdapterDialog(getActivity(), this, list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.show();

    }

    private void showDialogImages(ArrayList<MelaList> list) {
        click_status = true;
        dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.item_row, null);
        ListView lv = (ListView) view.findViewById(R.id.listView);

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterDialog clad = new CustomListAdapterDialog(getActivity(), this, list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.show();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void itemClicked(int position) {
        if (click_status) {
            Picasso.with(getActivity())
                    .load(itemsList1.get(position).getNormalPhotoUrl())
                    .into(imv_event, new Callback() {

                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            imv_event.setImageResource(R.drawable.img_loading_error);
                        }
                    });
            str_eimage = itemsList1.get(position).getNormalPhotoUrl();
        } else {
            txt_emela.setText(itemsList.get(position).getFullname());
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}