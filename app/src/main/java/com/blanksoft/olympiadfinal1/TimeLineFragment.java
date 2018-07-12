package com.blanksoft.olympiadfinal1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.activity.LoginActivity;
import com.blanksoft.olympiadfinal1.adapter.Adapter;
import com.blanksoft.olympiadfinal1.model.Content;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;


public class TimeLineFragment extends PostFragment {

    public TimeLineFragment() {
        // Required empty public constructor
    }

    /** @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_post, container, false);


        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsoncontent.clear();
                content();
                adapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // Inflate the layout for this fragment

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
    } **/


    @Override
    public void content(){
        final String URL =  "http://218.155.147.128:3000/content";
        mProgressDialog= new ProgressDialog(getActivity());
        mProgressDialog.setMessage("잠시만 기다려 주세요.");
        mProgressDialog.show();
        com.android.volley.toolbox.StringRequest request = new com.android.volley.toolbox.StringRequest(Request.Method.POST,
                URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("qweqwerqwt" ,response.toString());

                            JSONArray jsonarray = new JSONArray(response);
                            if(jsonarray.length() == 0){
                                mProgressDialog.dismiss();
                            }
                            for(int i =0; i<jsonarray.length(); i++) {


                                JSONObject jresponse = jsonarray.getJSONObject(i);
                                Log.d("b", String.valueOf(jresponse.optBoolean("Bcomplete")));

                                    byte[] bytePlainOrg = Base64.decode(jresponse.optString("image", "null"), 0);
                                    ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                                    Bitmap bm = BitmapFactory.decodeStream(inStream);


                                    //Log.d("ggw",jresponse.getString("content"));




                                    //JOBJ = jresponse.optJSONObject("Ulike");
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
                                if(String.valueOf(jresponse.optBoolean("Bcomplete")).equals("true")){
                                    SETSELEC = "1";


                                }else {
                                    SETSELEC = "0";
                                }

                                    //Log.d("qwtqt", subArray.toString());

                                    Log.d("like", SETLIKE);
                                    Content content2 = new Content(jresponse.optString("content", "text on no value"), jresponse.optString("name", "text on no value"), jresponse.optString("date", "text on no value"), bm, jresponse.optString("like".toString()), jresponse.optString("contentid"), SETLIKE, SETSELEC);
                                   // Content content = new Content(jresponse.getDouble("makerlat"), jresponse.getDouble("makerlong"));
                                    //Collections.reverse(jsoncontent);

                                    jsoncontent.add(content2);

                                    Log.d("qwt", content2.toString());


                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                if (adapter == null) {
                                    adapter = new Adapter(getContext(), jsoncontent);
                                    recyclerView.setAdapter(adapter);

                                }
                                layoutManager.setReverseLayout(true);
                                layoutManager.setStackFromEnd(true);
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
