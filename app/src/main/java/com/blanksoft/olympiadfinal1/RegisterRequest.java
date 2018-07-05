package com.blanksoft.olympiadfinal1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://172.20.10.3:3000/process/adduser";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String usernickname,String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", userID);
        parameters.put("password", userPassword);
        parameters.put("name", usernickname);
        parameters.put("email", userEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}