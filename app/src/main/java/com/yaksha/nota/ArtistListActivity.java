package com.yaksha.nota;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.yaksha.nota.adapter.SectionsPagerAdapter;
import com.yaksha.nota.common.ActivityBase;

public class ArtistListActivity extends ActivityBase {

    Toolbar mToolbar;

    ViewPager mViewPager;
    TabLayout mTabLayout;

    SectionsPagerAdapter adapter;

    private Boolean restore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artistlist);

        if (savedInstanceState != null) {

            restore = savedInstanceState.getBoolean("restore");

        } else {

            restore = false;
        }

        // Toolbar

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ViewPager

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InActiveArtistFragment(),getString(R.string.str_in_active_artist) );
        adapter.addFragment(new ActiveArtistFragment(),getString(R.string.str_active_artist));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        // TabLayout

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {

                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for(Fragment f : fragments){
                    if(f != null && f instanceof InActiveArtistFragment)
                        if (((InActiveArtistFragment)f).canGoBack()) {
                            ((InActiveArtistFragment)f).goBack();
                        } else {
                            super.onBackPressed();
                        }

                }

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // your code.

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof InActiveArtistFragment)
                if (((InActiveArtistFragment)f).canGoBack()) {
                    ((InActiveArtistFragment)f).goBack();
                } else {
                    super.onBackPressed();
                }

        }
    }
}
