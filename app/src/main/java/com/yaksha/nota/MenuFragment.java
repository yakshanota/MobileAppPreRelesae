package com.yaksha.nota;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;

import com.yaksha.nota.app.App;
import com.yaksha.nota.constants.Constants;


public class MenuFragment extends Fragment implements Constants {

    ImageLoader imageLoader;

    private ImageView mFriendsIcon, mGuestsIcon;

    private ImageView mNavGalleryIcon, mNavGroupsIcon, mNavFriendsIcon, mNavGuestsIcon, mNavMarketIcon, mNavNearbyIcon, mNavFavoritesIcon, mNavStreamIcon, mNavPopularIcon, mNavUpgradesIcon, mNavSettingsIcon,mNavEventIcon;

    private MaterialRippleLayout mNavGallery, mNavGroups, mNavStream, mNavFriends, mNavMarket, mNavGuests, mNavFavorites, mNavNearby, mNavPopular, mNavAds, mNavSettings;

    private MaterialRippleLayout mnav_artist,mnav_video,mnav_support,mnav_profile,mNavEvent;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        imageLoader = App.getInstance().getImageLoader();

        setHasOptionsMenu(false);

        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        getActivity().setTitle(R.string.nav_menu);

        mNavGallery = rootView.findViewById(R.id.nav_gallery);
        mNavGroups = rootView.findViewById(R.id.nav_groups);
        mNavFriends = rootView.findViewById(R.id.nav_friends);
        mNavGuests = rootView.findViewById(R.id.nav_guests);
        mNavMarket = rootView.findViewById(R.id.nav_market);
        mNavNearby = rootView.findViewById(R.id.nav_nearby);
        mNavFavorites = rootView.findViewById(R.id.nav_favorites);
        mNavStream = rootView.findViewById(R.id.nav_stream);
        mNavPopular = rootView.findViewById(R.id.nav_popular);
        mNavAds = rootView.findViewById(R.id.nav_ads);
        mNavSettings = rootView.findViewById(R.id.nav_settings);
        mNavEvent = rootView.findViewById(R.id.nav_event);

        mnav_artist = rootView.findViewById(R.id.nav_artist);
        mnav_video = rootView.findViewById(R.id.nav_video);
        mnav_support = rootView.findViewById(R.id.nav_support);
        mnav_profile = rootView.findViewById(R.id.nav_profile);


        // Counters

        mFriendsIcon = rootView.findViewById(R.id.nav_friends_count_icon);
        mGuestsIcon = rootView.findViewById(R.id.nav_guests_count_icon);

        // Icons

        mNavGalleryIcon = rootView.findViewById(R.id.nav_gallery_icon);
        mNavGroupsIcon = rootView.findViewById(R.id.nav_groups_icon);
        mNavFriendsIcon = rootView.findViewById(R.id.nav_friends_icon);
        mNavGuestsIcon = rootView.findViewById(R.id.nav_guests_icon);
        mNavMarketIcon = rootView.findViewById(R.id.nav_market_icon);
        mNavNearbyIcon = rootView.findViewById(R.id.nav_nearby_icon);
        mNavFavoritesIcon = rootView.findViewById(R.id.nav_favorites_icon);
        mNavStreamIcon = rootView.findViewById(R.id.nav_stream_icon);
        mNavPopularIcon = rootView.findViewById(R.id.nav_popular_icon);
        mNavUpgradesIcon = rootView.findViewById(R.id.nav_ads_icon);
        mNavSettingsIcon = rootView.findViewById(R.id.nav_settings_icon);
        mNavEventIcon = rootView.findViewById(R.id.nav_events_icon);

        if (!MARKETPLACE_FEATURE) {

            mNavMarket.setVisibility(View.GONE);
        }


        mNavGallery.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavGalleryIcon);
            }

            return false;
        });

        mNavGallery.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), GalleryActivity.class);
            i.putExtra("profileId", App.getInstance().getId());
            getActivity().startActivity(i);
        });

        mNavGroups.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavGroupsIcon);
            }

            return false;
        });

        mNavGroups.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), GroupsActivity.class);
            startActivity(i);
        });

        mNavFriends.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavFriendsIcon);
            }

            return false;
        });

        mNavFriends.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), FriendsActivity.class);
            i.putExtra("profileId", App.getInstance().getId());
            startActivity(i);
        });

        mNavGuests.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavGuestsIcon);
            }

            return false;
        });

        mNavGuests.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), GuestsActivity.class);
            startActivity(i);
        });

        mNavMarket.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavMarketIcon);
            }

            return false;
        });

        mNavMarket.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), MarketActivity.class);
            startActivity(i);
        });

        mNavNearby.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavNearbyIcon);
            }

            return false;
        });

        mNavNearby.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), NearbyActivity.class);
            startActivity(i);
        });

        mNavFavorites.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavFavoritesIcon);
            }

            return false;
        });

        mNavFavorites.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), FavoritesActivity.class);
            i.putExtra("profileId", App.getInstance().getId());
            startActivity(i);
        });

        mNavStream.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavStreamIcon);
            }

            return false;
        });

        mNavStream.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), StreamActivity.class);
            startActivity(i);
        });

        mNavPopular.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavPopularIcon);
            }

            return false;
        });

        mNavPopular.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), PopularActivity.class);
            startActivity(i);
        });

        mNavAds.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavUpgradesIcon);
            }

            return false;
        });

        mNavAds.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), AdvertiseActivity.class);
            startActivity(i);
        });

        mNavSettings.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavSettingsIcon);
            }

            return false;
        });

        mNavEvent.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                animateIcon(mNavEventIcon);
            }

            return false;
        });

        mNavEvent.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), EventActivity.class);
            startActivity(i);
        });

        mNavSettings.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        });

        mnav_artist.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), ArtistListActivity.class);
            startActivity(i);
        });

        mnav_video.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), VideoListActivity.class);
            startActivity(i);
        });

        mnav_support.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), SupportActivity.class);
            startActivity(i);
        });

        mnav_profile.setOnClickListener(view -> {

            Intent i = new Intent(getActivity(), ProfileActivity.class);
            startActivity(i);
        });


        updateView();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateView() {

        // Counters

        mFriendsIcon.setVisibility(View.GONE);
        mGuestsIcon.setVisibility(View.GONE);

        if (App.getInstance().getNewFriendsCount() != 0) {

            mFriendsIcon.setVisibility(View.VISIBLE);
        }

        if (App.getInstance().getGuestsCount() != 0) {

            mGuestsIcon.setVisibility(View.VISIBLE);
        }
    }

    private void animateIcon(ImageView icon) {

        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(175);
        scale.setInterpolator(new LinearInterpolator());

        icon.startAnimation(scale);
    }

    @Override
    public void onResume() {

        super.onResume();

        updateView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
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