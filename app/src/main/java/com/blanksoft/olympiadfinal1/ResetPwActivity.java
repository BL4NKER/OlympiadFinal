package com.blanksoft.olympiadfinal1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPwActivity extends AppCompatActivity {

    EditText etResetPw;
    Button btResetPw;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);

        etResetPw = (EditText) findViewById(R.id.etResetPw);
        btResetPw = (Button) findViewById(R.id.btResetPw);

        btResetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {

        email = etResetPw.getText().toString();

        final String URL =  "http://172.20.10.4:3000/process/email";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            String num = jsonObj.getString("num");
                            //Log.d("num", num);

                            if(num.equals("0")){
                                Toast.makeText(getApplicationContext(), "등록 된 이메일이 없습니다.", Toast.LENGTH_LONG).show();

                            }else{

                                email = jsonObj.getString("name");
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
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
                Log.d("email", email);

                Map<String, String> params = new HashMap<>();
                params.put("id", email);

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
