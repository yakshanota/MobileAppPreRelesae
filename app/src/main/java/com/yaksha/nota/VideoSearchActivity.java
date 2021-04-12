package com.yaksha.nota;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yaksha.nota.adapter.VideoListAdapter;
import com.yaksha.nota.app.App;
import com.yaksha.nota.common.ActivityBase;
import com.yaksha.nota.model.NewVideo;
import com.yaksha.nota.util.CustomRequestNew;
import com.yaksha.nota.view.LineItemDecoration;

public class VideoSearchActivity  extends ActivityBase implements SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_LIST = "State Adapter Data";

    Toolbar mToolbar;

    SearchView searchView = null;

    SwipeRefreshLayout mItemsContainer;

    LinearLayout mHeaderContainer;

    private RecyclerView mRecyclerView;
    private NestedScrollView mNestedView;

    TextView mMessage, mHeaderText;

    private ArrayList<NewVideo> itemsList;
    private VideoListAdapter itemsAdapter;

    public String queryText, currentQuery, oldQuery;

    public int itemsCount;
    Boolean loadingMore = false;
    Boolean viewMore = false;
    private int arrayLength = 0;
    private int itemId = 0;
    private Boolean restore = false;
    private Boolean preload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        mHeaderContainer = (LinearLayout) findViewById(R.id.container_header);

        mItemsContainer = (SwipeRefreshLayout) findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mHeaderText = (TextView) findViewById(R.id.headerText);
        mMessage = (TextView) findViewById(R.id.message);


        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new VideoListAdapter(VideoSearchActivity.this, itemsList);

            preload = savedInstanceState.getBoolean("preload");
            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getInt("itemId");
            itemsCount = savedInstanceState.getInt("itemsCount");
            currentQuery = queryText = savedInstanceState.getString("queryText");

        } else {

            itemsList = new ArrayList<NewVideo>();
            itemsAdapter = new VideoListAdapter(VideoSearchActivity.this, itemsList);

            preload = true;
            restore = false;
            itemId = 0;
            itemsCount = 0;
            currentQuery = queryText = "";
        }

        mNestedView = (NestedScrollView) findViewById(R.id.nested_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(itemsAdapter);

        itemsAdapter.setOnItemClickListener((view, item, position) -> {
            Intent i = new Intent(VideoSearchActivity.this, VideoViewActivity.class);
            i.putExtra("videoUrl", item.getVideoUrl());
            startActivity(i);
        });


        mRecyclerView.setNestedScrollingEnabled(false);


        mNestedView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY < oldScrollY) { // up


                }

                if (scrollY > oldScrollY) { // down


                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!loadingMore && (viewMore) && !(mItemsContainer.isRefreshing())) {

                        mItemsContainer.setRefreshing(true);

                        loadingMore = true;

                        search();
                    }
                }
            }
        });

        if (queryText.length() == 0) {

            if (mRecyclerView.getAdapter().getItemCount() == 0) {

                showMessage(getString(R.string.label_group_search_start_screen_msg));
                mHeaderContainer.setVisibility(View.GONE);

            } else {

                if (preload) {

                    mHeaderText.setVisibility(View.GONE);
                    mHeaderContainer.setVisibility(View.GONE);

                } else {

                    mHeaderContainer.setVisibility(View.GONE);
                    mHeaderText.setVisibility(View.GONE);
                    mHeaderText.setText(getText(R.string.label_group_search_results) + " " + Integer.toString(itemsCount));
                }

                hideMessage();
            }

        } else {

            if (mRecyclerView.getAdapter().getItemCount() == 0) {

                showMessage(getString(R.string.label_search_results_error));
                mHeaderContainer.setVisibility(View.GONE);

            } else {

                mHeaderContainer.setVisibility(View.GONE);
                mHeaderText.setText(getText(R.string.label_group_search_results) + " " + Integer.toString(itemsCount));

                hideMessage();
            }
        }
        if (!restore) {

            if (preload) {

                getItems();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("preload", preload);
        outState.putBoolean("restore", true);
        outState.putInt("itemId", itemId);
        outState.putInt("itemsCount", itemsCount);
        outState.putString("queryText", queryText);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
    }

    @Override
    public void onRefresh() {

        currentQuery = queryText;

        currentQuery = currentQuery.trim();

        if (App.getInstance().isConnected() && currentQuery.length() != 0) {

            itemId = 0;
            search();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    public void searchStart() {

        preload = false;

        currentQuery = getCurrentQuery();

        if (App.getInstance().isConnected()) {

            itemId = 0;
            search();

        } else {

            Toast.makeText(getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentQuery() {

        String searchText = searchView.getQuery().toString();
        searchText = searchText.trim();

        return searchText;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.options_menu_main_search);

        SearchManager searchManager = (SearchManager) VideoSearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {

            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        }

        if (searchView != null) {

            searchView.setQuery(queryText, false);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(VideoSearchActivity.this.getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setIconified(false);

            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
            searchAutoComplete.setHint(getText(R.string.placeholder_search));
            searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {

                    queryText = newText;

                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

                    queryText = query;
                    searchStart();

                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
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
    public void onBackPressed() {

        finish();
    }

    public void search() {

        mItemsContainer.setRefreshing(true);

        CustomRequestNew jsonReq = new CustomRequestNew(Request.Method.POST, METHOD_VIDEO_SEARCH, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            if (!loadingMore) {

                                itemsList.clear();
                            }

                            arrayLength = 0;

                            arrayLength = response.length();

                            if (arrayLength > 0) {

                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject profileObj = response.getJSONObject(i);

                                    NewVideo group = new NewVideo(profileObj);

                                    itemsList.add(group);
                                }
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

//
                        }
                    }
                }, error -> {

            loadingComplete();
            Toast.makeText(getApplicationContext(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("query", currentQuery);

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getItems() {

        mItemsContainer.setRefreshing(true);

        CustomRequestNew jsonReq = new CustomRequestNew(Request.Method.POST, METHOD_VIDEO_LIST, null,
                response -> {

                    if (!loadingMore) {

                        itemsList.clear();
                    }

                    try {

                        arrayLength = response.length();

                        if (arrayLength > 0) {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject userObj = response.getJSONObject(i);

                                NewVideo community = new NewVideo(userObj);

                                itemsList.add(community);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        loadingComplete();

                    }

                }, error -> {

            loadingComplete();
            Toast.makeText(getApplicationContext(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {

        if (arrayLength == LIST_ITEMS) {

            viewMore = true;

        } else {

            viewMore = false;
        }

        itemsAdapter.notifyDataSetChanged();

        loadingMore = false;

        mItemsContainer.setRefreshing(false);

        if (mRecyclerView.getAdapter().getItemCount() == 0) {

            showMessage(getString(R.string.label_search_results_error));
            mHeaderContainer.setVisibility(View.GONE);

        } else {

            hideMessage();

            if (preload) {

                mHeaderContainer.setVisibility(View.GONE);
                mHeaderText.setVisibility(View.GONE);

            } else {

                mHeaderContainer.setVisibility(View.GONE);
                mHeaderText.setVisibility(View.GONE);

                mHeaderText.setText(getText(R.string.label_group_search_results) + " " + Integer.toString(itemsCount));
            }
        }
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);
    }
}