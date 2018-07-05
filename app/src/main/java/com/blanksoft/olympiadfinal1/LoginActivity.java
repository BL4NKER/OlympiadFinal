package com.blanksoft.olympiadfinal1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText idText;
    EditText pwText;
    Button lognBtn;
    CheckBox checkBox;
    TextView register;
    String id;
    String pw;
    public static String userId;
    public static String userPw;
    public static String userName;
    boolean saveLoginData;
    SharedPreferences appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);


        idText =(EditText)findViewById(R.id.id);
        pwText =(EditText)findViewById(R.id.pw);
        lognBtn =(Button)findViewById(R.id.loginbtn);
        lognBtn.setOnClickListener(this);
        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (saveLoginData) {
            id = appData.getString("ID", "");
            pw = appData.getString("PWD", "");
            login();



        }







    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.loginbtn:
                id = idText.getText().toString();
                pw = pwText.getText().toString();
                Log.d("id", id);
                Log.d("pw", pw);
                save();

                login();
                break;

        }

    }

    private void validateName() {

    }

    private void validateEmail() {

    }

    private void validatePassword() {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.id:
                    validateName();
                    break;
                case R.id.pw:
                    validateEmail();
                    break;
            }
        }
    }
    private void login(){
        final String URL =  "http://172.20.10.4:3000/process/login";
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
                                Toast.makeText(getApplicationContext(), "니미", Toast.LENGTH_LONG).show();

                            }else{

                                userId = jsonObj.getString("id");
                                userPw = jsonObj.getString("pw");
                                userName = jsonObj.getString("name");
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
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
                Log.d("uid", id);
                Log.d("pw", pw);

                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("password", pw);


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

    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        editor.putString("PWD", pwText.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    public void onClickForgotPassword (View view) {
        Intent intent = new Intent(LoginActivity.this, ResetPwActivity.class);
        startActivity(intent);
    }
}