package com.blanksoft.olympiadfinal1.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.blanksoft.olympiadfinal1.activity.ChangePwActivity;
import com.blanksoft.olympiadfinal1.activity.LoginActivity;
import com.blanksoft.olympiadfinal1.activity.ProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    Button btHistory;
    Button btLikePost;
    Button btLogout;
    Button btChangePw;
    ImageView pImage;
    TextView name;
    TextView email;
    TextView rank;
    TextView score;
    int IMG_REQUEST = 0;
    PopupMenu popup;
    String profileImageBase64;
    Bitmap bitmap;
    String data;
    Bitmap profileBitmap;

    ProgressDialog mProgressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        btHistory = (Button) v.findViewById(R.id.btHistory);
        btLikePost = (Button) v.findViewById(R.id.btLikePost);
        btLogout = (Button) v.findViewById(R.id.btLogout);
        btChangePw = (Button) v.findViewById(R.id.changepw);
        name = (TextView) v.findViewById(R.id.name);
        name.setText(LoginActivity.userName);
        email = (TextView) v.findViewById(R.id.email);
        email.setText(LoginActivity.userEmail);
        pImage = (ImageView) v.findViewById(R.id.profileImage);
        rank = (TextView) v.findViewById(R.id.rank);
        score = (TextView) v.findViewById(R.id.score);
        final String URL = "http://218.155.147.128:3000/profileimage";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("image").equals("null")) {
                                pImage.setImageResource(R.drawable.ic_user);
                            } else {

                                byte[] bytePlainOrg = Base64.decode(jsonObject.optString("image"), 0);
                                RequestOptions requestOptions = new RequestOptions();

                                Glide.with(getContext())
                                        .asBitmap()
                                        .load(bytePlainOrg)
                                        .apply(requestOptions.fitCenter().circleCrop())
                                        .into(pImage);
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
                params.put("userid", LoginActivity.userId);
                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(request);
        final String URL1 = "http://218.155.147.128:3000/process/ranking";

        StringRequest request1 = new StringRequest(Request.Method.POST,
                URL1,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.getString("name").equals(LoginActivity.userName)) {
                                    rank.setText(jsonObject.getString("rank"));
                                    score.setText(jsonObject.getString("score"));
                                    break;
                                }
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

        };
        request1.setShouldCache(true);
        request1.setRetryPolicy(new DefaultRetryPolicy(

                200000,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(request1);
        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(getContext(), pImage);

                popup.inflate(R.menu.menu_profile);

                if (pImage == null) {
                    popup.getMenu().getItem(2).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_profile_camera:
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, 1);
                                return true;
                            case R.id.menu_profile_album:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, IMG_REQUEST);
                                return true;
                            case R.id.menu_profile_delete:
                                profileImageBase64 = "null";
                                pImage.setImageResource(R.drawable.ic_user);
                                send();

                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        btChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext()/*현재 액티비티 위치*/, ChangePwActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("lorh", "histroy");
                startActivity(i);
            }
        });
        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext()/*현재 액티비티 위치*/, ProfileActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("lorh", "histroy");
                startActivity(i);
            }
        });

        btLikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext()/*현재 액티비티 위치*/, ProfileActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("lorh", "like");
                startActivity(i);
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
                SharedPreferences mPref = getActivity().getSharedPreferences("appData", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPref.edit();
                boolean saveLd;
                saveLd = mPref.getBoolean("SAVE_LOGIN_DATA", false);
                if(saveLd){
                    editor.remove("SAVE_LOGIN_DATA");
                    editor.remove("ID");
                    editor.remove("PWD");
                    editor.commit();

                }

            }
        });
        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void Logout() {
        new AlertDialog.Builder(getContext())
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getContext()/*현재 액티비티 위치*/, LoginActivity.class/*이동 액티비티 위치*/);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public void send() {
        final String URL = "http://218.155.147.128:3000/process/profileimage";
        StringRequest request = new StringRequest(Request.Method.POST,
                URL, null, null)
        {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
                params.put("userid", LoginActivity.userId);
                params.put("image", profileImageBase64);
                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(request);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), path);
                Log.d("btmp", bitmap.toString());
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
                byte[] image = outStream.toByteArray();
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .apply(requestOptions.fitCenter().circleCrop())
                        .into(pImage);
                profileImageBase64 = Base64.encodeToString(image, 0);
                send();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
                byte[] image = outStream.toByteArray();
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .apply(requestOptions.fitCenter().circleCrop())
                        .into(pImage);
                profileImageBase64 = Base64.encodeToString(image, 0);
                send();


            }

        } else if (requestCode == 1) {
            try {

                profileBitmap = (Bitmap) data.getExtras().get("data");
                pImage.setImageBitmap(profileBitmap);
                pImage.setVisibility(View.VISIBLE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
                byte[] image = outStream.toByteArray();
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .apply(requestOptions.fitCenter().circleCrop())
                        .into(pImage);
                profileImageBase64 = Base64.encodeToString(image, 0);
                send();


            } catch (Exception e) {


            } catch (OutOfMemoryError e) {
                profileBitmap = (Bitmap) data.getExtras().get("data");
                pImage.setImageBitmap(profileBitmap);
                pImage.setVisibility(View.VISIBLE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
                byte[] image = outStream.toByteArray();
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(this)
                        .asBitmap()
                        .load(image)
                        .apply(requestOptions.fitCenter().circleCrop())
                        .into(pImage);
                profileImageBase64 = Base64.encodeToString(image, 0);
                send();


            }
        }
    }
}
