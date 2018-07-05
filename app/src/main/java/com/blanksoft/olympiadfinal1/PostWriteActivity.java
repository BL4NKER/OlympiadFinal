package com.blanksoft.olympiadfinal1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostWriteActivity extends AppCompatActivity {
    EditText etContentadd;
    String contentadd;

    String markerlat;
    String markerlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        etContentadd = (EditText)findViewById(R.id.etContent);

        Intent intent = new Intent(this.getIntent());

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        markerlat = intent.getStringExtra("markerlat");
        markerlong = intent.getStringExtra("markerlng");

        Log.d(markerlat + " " + markerlong, "lat/lng: ");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_post:
                Toast.makeText(this, "1111",Toast.LENGTH_SHORT).show();
                final String URL =  "http://172.20.10.4:3000/process/contentadd";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(response.equals("1")) {
                                        Intent i = new Intent(PostWriteActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "1111",Toast.LENGTH_SHORT).show();
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
                        contentadd = etContentadd.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        String uploadTime = sdf.format(cal.getTime());

                        Map<String, String> params = new HashMap<>();
                        params.put("content", contentadd);
                        params.put("name", LoginActivity.userName);
                        params.put("date", uploadTime);
                        params.put("lat", markerlat);
                        params.put("long", markerlong);

                        Log.d(markerlat + " " + markerlong, "lat/lng: ");

                        return params;
                    }
                };

                request.setShouldCache(true);
                Volley.newRequestQueue(this).add(request);


                break;

        }
        return true;
    }

}
