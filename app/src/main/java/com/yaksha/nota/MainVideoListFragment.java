package com.yaksha.nota;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yaksha.nota.adapter.VideoListAdapter;
import com.yaksha.nota.app.App;
import com.yaksha.nota.constants.Constants;
import com.yaksha.nota.model.NewVideo;
import com.yaksha.nota.util.CustomRequestNew;
import com.yaksha.nota.view.LineItemDecoration;

public class MainVideoListFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_LIST = "State Adapter Data";

    private RecyclerView mRecyclerView;
    private NestedScrollView mNestedView;

    private TextView mMessage;
    private ImageView mSplash;

    private SwipeRefreshLayout mItemsContainer;

    private ArrayList<NewVideo> itemsList;
    private VideoListAdapter itemsAdapter;

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;

    private EditText searchView;
    public String queryText = "";

    public MainVideoListFragment() {
        // Required empty public constructor
    }

    public MainVideoListFragment newInstance(Boolean pager) {

        MainVideoListFragment myFragment = new MainVideoListFragment();

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

        View rootView = inflater.inflate(R.layout.fragment_groups_search, container, false);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new VideoListAdapter(getActivity(), itemsList);

            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getInt("itemId");

        } else {

            itemsList = new ArrayList<NewVideo>();
            itemsAdapter = new VideoListAdapter(getActivity(), itemsList);

            restore = false;
            itemId = 0;
        }


        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mMessage = (TextView) rootView.findViewById(R.id.message);
        mSplash = (ImageView) rootView.findViewById(R.id.splash);

        searchView = (EditText) rootView.findViewById(R.id.et_search);

        //
        searchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_SEARCH);
        searchView.setHint(getString(R.string.placeholder_toolbar_search));


        mNestedView = (NestedScrollView) rootView.findViewById(R.id.nested_view);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(itemsAdapter);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "kkkkkkkk", Toast.LENGTH_LONG).show();
            }
        });

        itemsAdapter.setOnItemClickListener((view, item, position) -> {
            if (item.getVideoUrl().length() != 0) {
                watchYoutubeVideo(item.getVideoUrl());

            }

        });


        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchView.clearFocus();
                    //  hideKeyboard();

                    queryText = searchView.getText().toString().trim();
                    if (queryText.length() > 0) {

                        searchStart();

                    }


                    return true;
                }

                return false;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    //got focus

                    searchView.setCursorVisible(true);

                } else {

                    searchView.setCursorVisible(false);
                    searchView.clearFocus();
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }
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

                        getItems();
                    }
                }
            }
        });

        if (itemsAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }

        if (!restore) {

            showMessage(getText(R.string.msg_loading_2).toString());
            //  String val = Long.toString(App.getInstance().getId());
            //   String acc = App.getInstance().getAccessToken();
            getItems();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void watchYoutubeVideo(String id) {

        final String regex = "v=([^\\s&#]*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(id);
        String new_id = "";
        if (matcher.find()) {
            new_id = matcher.group(1);
        }

        if (new_id.length() > 0) {

            if (YOUTUBE_API_KEY.length() > 5) {

                Intent i = new Intent(getActivity(), ViewYouTubeVideoActivity.class);
                i.putExtra("videoCode", new_id);
                startActivity(i);

            } else {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + new_id));
                    startActivity(intent);

                } catch (ActivityNotFoundException ex) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + new_id));
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            itemId = 0;
            getItems();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putInt("itemId", itemId);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
    }


    public void loadingComplete() {

        if (arrayLength == LIST_ITEMS) {

            viewMore = true;

        } else {

            viewMore = false;
        }

        itemsAdapter.notifyDataSetChanged();

        if (itemsAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }

        loadingMore = false;
        mItemsContainer.setRefreshing(false);
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);

        mSplash.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);

        mSplash.setVisibility(View.GONE);
    }

    public void searchStart() {

        if (App.getInstance().isConnected()) {

            itemId = 0;
            search();

        } else {

            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("query", queryText);

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
            Toast.makeText(getActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
