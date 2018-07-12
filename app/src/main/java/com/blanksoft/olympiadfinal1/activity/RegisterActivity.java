package com.blanksoft.olympiadfinal1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.fragment.RegisterRequest;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    AppCompatEditText idText;
    AppCompatEditText passwordText;
    AppCompatEditText nicknameText;
    AppCompatEditText emailText ;
    AppCompatEditText cfText;
    TextInputLayout nameLayout;
    TextInputLayout passwordLayout;
    TextInputLayout idLayout;
    TextInputLayout emailLayout;
    TextInputLayout cfLayout;
    ProgressDialog mProgressDialog;
    private Handler mHandler;

    String password;

    TextWatcher idTextWatcher = new TextWatcher()
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
            String ID = idText.getText().toString();
            if (!Pattern.matches( "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", ID)) {
                idLayout.setError("잘못된 아이디 형식입니다. (영문으로 시작하며 영문, 숫자, '_' 만을 포함한 5 ~ 12자리 조합)");
            } else {
                idLayout.setErrorEnabled(false);
            }

        }
    };


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
    TextWatcher passwordTextWatcher = new TextWatcher()
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

            password = passwordText.getText().toString();
            if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,14}$", password)) {
                passwordLayout.setError("잘못된 비밀번호 형식입니다. (숫자, 특수문자, 알파벳만을 포함한 8~14자리 조합)");
            } else {
                passwordLayout.setErrorEnabled(false);
            }

        }
    };

    TextWatcher cfTextWatcher = new TextWatcher()
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

            String motherfuck = passwordText.getText().toString();
            String cfpw = cfText.getText().toString();
            if (!cfpw.equals(motherfuck)) {
                cfLayout.setError("비밀번호가 일치하지 않습니다");
            }
            else {
                cfLayout.setErrorEnabled(false);
            }
        }
    };

    TextWatcher nameTextWatcher = new TextWatcher()
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

            String name = nicknameText.getText().toString();
            if (name == null || name.length() < 2) {
                nameLayout.setError("잘못된 이름 형식입니다. (2자리 이상의 조합) ");
            } else {
                nameLayout.setErrorEnabled(false);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        idText = (AppCompatEditText) findViewById(R.id.IDtv);
        passwordText = (AppCompatEditText)findViewById(R.id.Passwordtv);
        nicknameText = (AppCompatEditText) findViewById(R.id.Nametv);
        cfText = (AppCompatEditText) findViewById(R.id.CFtv);
        final Button alreadyhaveButton = (Button)findViewById(R.id.AHAA);
        emailText = (AppCompatEditText)findViewById(R.id.Emailtv);
        final Button registerButton = (Button)findViewById(R.id.signUp);
        nameLayout = (TextInputLayout) findViewById(R.id.Nameti);
        passwordLayout = (TextInputLayout) findViewById(R.id.Passwordti);
        idLayout = (TextInputLayout) findViewById(R.id.IDti);
        emailLayout = (TextInputLayout) findViewById(R.id.Emailti);
        cfLayout = (TextInputLayout) findViewById(R.id.CFti);

        idText.addTextChangedListener(idTextWatcher);
        passwordText.addTextChangedListener(passwordTextWatcher);
        nicknameText.addTextChangedListener(nameTextWatcher);
        emailText.addTextChangedListener(emailTextWatcher);
        cfText.addTextChangedListener(cfTextWatcher);

        alreadyhaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHandler = new Handler();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPassword = cfText.getText().toString();
                String usernickname = nicknameText.getText().toString();
                String userEmail = emailText.getText().toString();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mProgressDialog = ProgressDialog.show(RegisterActivity.this,"",
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
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            String a = response;
                            //boolean success = jsonResponse.getBoolean("success");
                            if (a.equals("0")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원등록에 성공했습니다")
                                        .setPositiveButton("확인",null)
                                        .create()
                                        .show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            }
                            else if (a.equals("1"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("아이디를 사용 할 수 없습니다")
                                        .setNegativeButton("다시 시도해주세요",null)
                                        .create()
                                        .show();
                            }
                            else if (a.equals("2"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("이메일을 사용 할 수 없습니다")
                                        .setNegativeButton("다시 시도해주세요",null)
                                        .create()
                                        .show();
                            }
                            else if (a.equals("3"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("닉네임을 사용 할 수 없습니다")
                                        .setNegativeButton("다시 시도해주세요",null)
                                        .create()
                                        .show();
                            }
                            mProgressDialog.dismiss();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword , usernickname, userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}
