package com.yaksha.nota;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;
import com.yaksha.nota.common.ActivityBase;

public class EventViewerActivity extends ActivityBase {

    private ImageView photo;
    private TextView event_name, event_desc, event_date, event_place;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventviewer);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        photo = findViewById(R.id.video_image);
        event_name = findViewById(R.id.username);
        event_desc = findViewById(R.id.txt_date);
        event_date = findViewById(R.id.txt_desc);
        event_place = findViewById(R.id.txt_place);


        event_name.setText(getIntent().getStringExtra("EventName"));
        event_desc.setText(getIntent().getStringExtra("Desc"));
        event_place.setText(getIntent().getStringExtra("EventMela"));
        event_date.setText(getIntent().getStringExtra("Date"));
        Picasso.with(EventViewerActivity.this)
                .load(getIntent().getStringExtra("Image"))
                .into(photo, new Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        photo.setImageResource(R.drawable.img_loading_error);
                    }
                });


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
}
