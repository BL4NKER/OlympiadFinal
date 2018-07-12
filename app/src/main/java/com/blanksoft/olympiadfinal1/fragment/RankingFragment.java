package com.blanksoft.olympiadfinal1.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.adapter.RankingAdapter;
import com.blanksoft.olympiadfinal1.model.Ranking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    List<Ranking> rankingList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    ProgressDialog mProgressDialog;

    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler);
        rankingList = new ArrayList<>();
        final String URL = "http://218.155.147.128:3000/process/ranking";

        mProgressDialog= new ProgressDialog(getActivity());
        mProgressDialog.setMessage("잠시만 기다려 주세요.");
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Ranking ranking = new Ranking(jsonObject.optString("score"), jsonObject.optString("rank"), jsonObject.getString("name"));
                                rankingList.add(ranking);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                if (adapter == null) {
                                    adapter = new RankingAdapter(getContext(), rankingList);
                                    recyclerView.setAdapter(adapter);

                                }
                                mProgressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {

        };
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(

                200000,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(request);
        return v;
    }
}
