package com.yaksha.nota;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.yaksha.nota.adapter.SectionsPagerAdapter;

public class ArtistFragment extends Fragment  {

    private static final String STATE_LIST = "State Adapter Data";
    Toolbar mToolbar;

    ViewPager mViewPager;
    TabLayout mTabLayout;

    SectionsPagerAdapter adapter;

    private Boolean restore = false;
    private FragmentActivity myContext;

    public ArtistFragment() {
        // Required empty public constructor
    }

    public ArtistFragment newInstance(Boolean pager) {

        ArtistFragment myFragment = new ArtistFragment();

        Bundle args = new Bundle();
        args.putBoolean("pager", pager);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            restore = savedInstanceState.getBoolean("restore");

        } else {

            restore = false;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_artistlist_fragment, container, false);


        // Toolbar

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);



        // ViewPager

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);

        adapter = new SectionsPagerAdapter(myContext.getSupportFragmentManager());
        adapter.addFragment(new ActiveArtistFragment(), getString(R.string.str_active_artist));
        adapter.addFragment(new InActiveArtistFragment(),getString(R.string.str_in_active_artist));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        // TabLayout

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                getActivity().finish();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }



    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}