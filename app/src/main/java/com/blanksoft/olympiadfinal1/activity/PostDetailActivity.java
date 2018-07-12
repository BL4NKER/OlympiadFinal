package com.blanksoft.olympiadfinal1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.adapter.CompleteAdapter;
import com.blanksoft.olympiadfinal1.model.Complete;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    List<Complete> jsoncomplete;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    TextView tvAuthor;
    TextView tvContent;
    TextView tvCountLike;
    TextView tvDate;
    ImageButton btPopup;
    String name;
    ImageView profileImage;
    ImageView chk;
    ImageView image;
    CheckBox chLike;
    JSONObject JOBJ;
    JSONArray Array;
    JSONArray UsersArray;
    String SETLIKE = "0";
    String SETSELEC = "0";
    private Handler mHandler;
    ProgressDialog mProgressDialog;

    ViewGroup cmlayout;


    Intent intent2;
    String contentId;
    String likechk;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mHandler = new Handler();

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mProgressDialog = ProgressDialog.show(PostDetailActivity.this,"",
                        "잠시만 기다려 주세요.",true);
                mHandler.postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if (mProgressDialog!=null&&mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }
                        catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                }, 100000);
            }
        } );



        final Intent intent = getIntent();
        intent2 = new Intent(this ,CommentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        tvAuthor = (TextView) findViewById(R.id.name);
        chLike = (CheckBox) findViewById(R.id.checkBoxLike);
        tvContent = (TextView) findViewById(R.id.content1);
        tvCountLike = (TextView) findViewById(R.id.tvLikeCount);
        tvDate = (TextView) findViewById(R.id.date);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        cmlayout = (ViewGroup) findViewById(R.id.comment);
        btPopup = (ImageButton) findViewById(R.id.imageButton2);
        image = (ImageView) findViewById(R.id.image);
        chk = (ImageView) findViewById(R.id.check);



       cmlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                intent2.putExtra("contentId", contentId);
                intent2.putExtra("kind", "content");
                startActivity(intent2);


            }
        });


        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.cmrecycler);

        jsoncomplete = new ArrayList<>();

        name = intent.getStringExtra("name");
        image();

        contentId = intent.getStringExtra("contentid");
        //adapter = new CompleteAdapter(contentId);
        final String URL = "http://218.155.147.128:3000/process/coment";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d("jsonobh", jsonObject.toString());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(jsonObject.getString("image") == null){
                        image.setImageResource(0);
                    }else{
                        byte[] bytePlainOrg = Base64.decode(jsonObject.optString("image"), 0);
                        RequestOptions requestOptions = new RequestOptions();

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(bytePlainOrg)
                                .into(image);
                    }
                    tvAuthor.setText(jsonObject.optString("name", "text"));
                    tvContent.setText(jsonObject.optString("content", "text"));
                    tvCountLike.setText(jsonObject.optString("like", "text"));
                    tvDate.setText(jsonObject.optString("date", "text"));
                    if(String.valueOf(jsonObject.optBoolean("Bcomplete")).equals("false")){
                        chk.setVisibility(View.GONE);
                    }else {
                        chk.setVisibility(View.VISIBLE);
                    }
                    JSONArray Array;
                    Array = jsonObject.optJSONArray("likeUsers");
                    SETLIKE = "1";
                    if (Array.length() > 0) {
                        for (int j = 0; j < Array.length(); j++) {
                            SETLIKE = "1";
                            if (Array.get(j).equals(LoginActivity.userId)) {
                                SETLIKE = "0";
                                Log.d("yeeag", "jj");
                                break;
                            }

                        }
                    }
                    if(SETLIKE.equals("0")){
                        chLike.setChecked(true);
                    }else {
                        chLike.setChecked(false);
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
        )
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("contentid", contentId);

                return params;
            }
        };
        request.setShouldCache(true);
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Log.d("contentid", contentId);

        /*if(author.equals(LoginActivity.userName)){
            bo.setVisibility(View.GONE);
        }*/






        btPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), btPopup);

                popup.inflate(R.menu.menu_item);

                if (!LoginActivity.userName.equals(name)) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_delete:



                                final String URL = "http://218.155.147.128:3000/process/remove";
                                StringRequest request = new StringRequest(Request.Method.POST,
                                        URL,new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            mProgressDialog.dismiss();
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
                                )
                                {
                                    @Override
                                    protected Map<String, String> getParams() {

                                        Map<String, String> params = new HashMap<>();
                                        params.put("contentid", contentId);

                                        return params;
                                    }
                                };
                                request.setShouldCache(true);
                                Volley.newRequestQueue(getApplicationContext()).add(request);
                                Intent i = new Intent(PostDetailActivity.this, MainActivity.class);
                                startActivity(i);

                                return true;

                            case R.id.menu_modify:

                                Intent intent = new Intent(getApplicationContext(), ModifyActivity.class);
                                intent.putExtra("modifyText", tvContent.getText());
                                intent.putExtra("contentid", contentId);
                                intent.putExtra("kind", "post");

                                startActivity(intent);
                                //((MainActivity)getApplicationContext()).finish();
                        }
                        return false;


                    }
                });

                popup.show();
            }
        });

        final String URL1 =  "http://218.155.147.128:3000/complete";

        StringRequest request1 = new StringRequest(Request.Method.POST,
                URL1,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonarray = new JSONArray(response);


                                    for(int i =0; i<jsonarray.length(); i++) {

                                        JSONObject jresponse = jsonarray.getJSONObject(i);
                                        if (jresponse.optString("registContentId").equals(contentId)){
                                            byte[] bytePlainOrg = Base64.decode(jresponse.optString("image", ""), 0);

                                        if(String.valueOf(jresponse.optBoolean("Bselection")).equals("true")){
                                            SETSELEC = "1";


                                        }else {
                                            SETSELEC = "0";
                                        }
                                        ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                                            BitmapFactory.Options resample = new BitmapFactory.Options();
                                            resample.inSampleSize = 1/2;

                                            Bitmap bm = BitmapFactory.decodeStream(inStream, null,resample);




                                        //Log.d("ggw",jresponse.getString("content"));

                                        String Test = "[{\"TV\":\"3d tv\"},{\"computer\":\"Computer\notebook\",\"LG\":\"1\",\"count\":\"20\"}]";




                                        //Log.d("qwtqt", subArray.toString());
                                            JSONArray Array;
                                            Array = jresponse.optJSONArray("likeUsers");
                                            // String SETCHECK;
//                                JSONArray subArray = new JSONArray(JOBJ.get("userid").toString());
                                            SETLIKE = "1";
                                            if (Array.length() > 0) {
                                                for (int j = 0; j < Array.length(); j++) {
//                                     String val = (String)Array.get(j);
                                                    SETLIKE = "1";

                                                    if (Array.get(j).equals(LoginActivity.userId)) {
                                                        SETLIKE = "0";
                                                        Log.d("yeeag", "jj");
                                                        break;
                                                    }

                                                }
                                            }


                                        Complete complete = new Complete(jresponse.optString("content", "text on no value"), jresponse.optString("receptionUser", "text on no value"), jresponse.optString("registUser", "text on no value"), jresponse.optString("date", "text on no value"), bm, jresponse.optString("like".toString()), jresponse.optString("contentid"), SETLIKE, SETSELEC);

                                        //Collections.reverse(jsoncontent);
                                            Log.d("iididd", jresponse.optString("contentid"));

                                        jsoncomplete.add(complete);


                                    }

                                    }


                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                if (adapter == null) {
                                    adapter = new CompleteAdapter(getApplicationContext(), jsoncomplete);
                                    recyclerView.setAdapter(adapter);

                                }
                                mProgressDialog.dismiss();
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
        request1.setShouldCache(true);
        request1.setRetryPolicy(new DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request1);
    }
    public void image(){
        final String URL =  "http://218.155.147.128:3000/profileimage";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("image").equals("null")){
                                profileImage.setImageResource(R.drawable.user);
                            }else {

                                byte[] bytePlainOrg = Base64.decode(jsonObject.optString("image"), 0);
                                RequestOptions requestOptions = new RequestOptions();

                                Glide.with(PostDetailActivity.this)
                                        .asBitmap()
                                        .load(bytePlainOrg)
                                        .apply(requestOptions.fitCenter().circleCrop())
                                        .into(profileImage);
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
                params.put("userid", name);
                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);

    }

}
