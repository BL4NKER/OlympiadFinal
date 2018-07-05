package com.blanksoft.olympiadfinal1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class LikePostFragment extends PostFragment {

    public LikePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void content(){
        final String URL =  "";

        com.android.volley.toolbox.StringRequest request = new com.android.volley.toolbox.StringRequest(Request.Method.POST,
                URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("qweqwerqwt" ,response.toString());

                            JSONArray jsonarray = new JSONArray(response);
                            for(int i =0; i<jsonarray.length(); i++){

                                JSONObject jresponse = jsonarray.getJSONObject(i);
                                byte[] bytePlainOrg = Base64.decode(jresponse.optString("image", "iVBORw0KGgoAAAANSUhEUgAAATkAAAE5CAYAAADr4VfxAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJ\n" +
                                        "bWFnZVJlYWR5ccllPAAABqFJREFUeNrs3T1vW1UcwOHjJKgDoRjlA8QlLEiohImBAVtMWWgKO20n"), 0);

                                ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                                Bitmap bm = BitmapFactory.decodeStream(inStream) ;

                                //Log.d("ggw",jresponse.getString("content"));

                                String Test = "[{\"TV\":\"3d tv\"},{\"computer\":\"Computer\notebook\",\"LG\":\"1\",\"count\":\"20\"}]";


                                //JOBJ = jresponse.optJSONObject("Ulike");
                                Array = jresponse.optJSONArray("likeUsers");
                                // String SETCHECK;
//                                JSONArray subArray = new JSONArray(JOBJ.get("userid").toString());
                                SETLIKE = "1";
                                if(Array.length() >0){
                                    for(int j = 0; j < Array.length(); j++) {
//                                     String val = (String)Array.get(j);
                                        SETLIKE = "1";

                                        if (Array.get(j).equals(LoginActivity.userId)) {
                                            SETLIKE = "0";
                                            Log.d("yeeag", "jj");
                                            break;
                                        }

                                    }
                                }

                                //Log.d("qwtqt", subArray.toString());

                                Log.d("like",SETLIKE);
                                Content content2 =new Content(jresponse.optString("content", "text on no value"), jresponse.optString("name", "text on no value"), jresponse.optString("date", "text on no value"), bm, jresponse.optString("like".toString()), jresponse.optString("contentid"), SETLIKE);

                                //Collections.reverse(jsoncontent);

                                jsoncontent.add(content2);

                                Log.d("qwt", content2.toString());

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            if(adapter == null) {
                                adapter = new Adapter(getContext(), jsoncontent);
                                recyclerView.setAdapter(adapter);

                            }
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);


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



                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(request);
    }
}
