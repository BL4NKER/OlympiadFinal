package com.blanksoft.olympiadfinal1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.blanksoft.olympiadfinal1.model.Content;

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
    ProgressDialog mProgressDialog;


    JSONObject JOBJ;
    String SETLIKE="1";
    String SETSELEC = "0";

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
        //return inflater.inflate(R.layout.fragment_post, container, false);

        View v = inflater.inflate(R.layout.fragment_post, container, false);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                jsoncontent.clear();
                content();
                if(adapter!=null)
                adapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        jsoncontent = new ArrayList<>();
        content();

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void content(){
    }
}
