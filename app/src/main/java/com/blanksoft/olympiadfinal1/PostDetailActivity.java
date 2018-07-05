package com.blanksoft.olympiadfinal1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    List<Comment> jsoncomment;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    TextView tvAuthor;
    TextView tvContent;
    TextView tvCountLike;
    Button Btn;
    CheckBox chLike;
    JSONObject JOBJ;
    JSONArray Array;
    JSONArray UsersArray;

    Button btDelete;

    String contentId;
    String likechk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();

        tvAuthor = (TextView) findViewById(R.id.name);
        chLike = (CheckBox) findViewById(R.id.checkBoxLike);
        tvContent = (TextView) findViewById(R.id.content1);
        tvCountLike = (TextView) findViewById(R.id.tvLikeCount);

        btDelete = (Button) findViewById(R.id.btDelete);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.cmrecycler);

        jsoncomment = new ArrayList<>();

        String author = intent.getStringExtra("author");
        contentId = intent.getStringExtra("contentid");
        Log.d("contentid", contentId);
        likechk = intent.getStringExtra("likechk");
        String content = intent.getStringExtra("content");
        String countLike = intent.getStringExtra("countLike");
        Log.d("rqwt", contentId);

        if(likechk.equals("0")){
            chLike.setChecked(true);
        }else {
            chLike.setChecked(false);
        }

        if (LoginActivity.userName.equals(author)) {
            btDelete.setVisibility(View.VISIBLE);
        } else {
            btDelete.setVisibility(View.GONE);
        }

        tvAuthor.setText(author);
        tvContent.setText(content);
        tvCountLike.setText(countLike);

        final String URL =  "http://172.20.10.4:3000/process/coment";

        StringRequest request = new StringRequest(Request.Method.POST,
                URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObj = new JSONObject(response);

                            Array = jsonObj.optJSONArray("coment");
                            UsersArray = jsonObj.optJSONArray("users");

                            JSONArray subArray = new JSONArray(jsonObj.get("coment").toString());
                            for(int j = 0; j < subArray.length(); j++) {

                                Comment comment = new Comment(Array.getString(j), UsersArray.getString(j));
                                Log.d("comment", Array.getString(j));
                                jsoncomment.add(comment);

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            if(adapter == null) {
                                adapter = new CommentAdapter(getApplicationContext(), jsoncomment);
                                recyclerView.setAdapter(adapter);

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
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("contentid", contentId);

                return params;
            }
        };
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

}
