package com.blanksoft.olympiadfinal1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    List<Content> jsoncontent;
    ImageView image;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    CheckBox chklike;
    String userid;
    JSONArray Array;
    JSONObject mArray;
    char[] uid;

    JSONObject JOBJ;
    String SETLIKE="1";

    LinearLayoutManager layoutManager;


    public PostFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);

        Toolbar mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsoncontent.clear();
                content();
                adapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        chklike = (CheckBox) findViewById(R.id.checkBoxLike);

        jsoncontent = new ArrayList<>();
        content();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
