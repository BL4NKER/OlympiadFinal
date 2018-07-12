package com.blanksoft.olympiadfinal1.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ResetPwActivity extends AppCompatActivity {

    AppCompatEditText emailText ;
    TextInputLayout emailLayout;
    Button btResetPw;
    Button btBack;

    TextWatcher emailTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }
        @Override
        public void afterTextChanged(Editable s)
        {

            String email = emailText.getText().toString();
            if (!Pattern.matches("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([A-z]*)$", email)) {
                emailLayout.setError("잘못된 이메일 형식입니다.");
            }
            else emailLayout.setErrorEnabled(false);

        }
    };

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);

        emailText = (AppCompatEditText) findViewById(R.id.Emailtv);
        emailLayout = (TextInputLayout) findViewById(R.id.Emailti);
        btResetPw = (Button) findViewById(R.id.btResetPw);

        emailText.addTextChangedListener(emailTextWatcher);
        btBack = (Button) findViewById(R.id.btBack);

        btResetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword() {

        email = emailText.getText().toString();

        final String URL =  "http://218.155.147.128:3000/process/email";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            String req = response;
                            //Log.d("num", num);

                            if(req.equals("0")){
                                Toast.makeText(getApplicationContext(), "등록 된 이메일이 없습니다.", Toast.LENGTH_LONG).show();

                            }else{

                                Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.이메일을 확인해주세요", Toast.LENGTH_LONG).show();
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
                params.put("email", email);

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
