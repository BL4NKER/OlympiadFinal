package com.blanksoft.olympiadfinal1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostWriteActivity extends AppCompatActivity {
    EditText etContentadd;
    String contentadd;

    String markerlat;
    String markerlong;
    private Handler mHandler;
    ProgressDialog mProgressDialog;
    /** 카메라 변수 **/
    String profileImageBase64;
    ImageView ivImage;
    String deCode;
    Bitmap bitmap;
    String data;
    Bitmap profileBitmap;
    private final int IMG_REQUEST = 2;

    private void Decode(String dec){
        String data = dec;

        //데이터 base64 형식으로 Decode
        String txtPlainOrg = "";
        byte[] bytePlainOrg = Base64.decode(data, 0);

        //byte[] 데이터  stream 데이터로 변환 후 bitmapFactory로 이미지 생성
        ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
        Bitmap bm = BitmapFactory.decodeStream(inStream) ;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        etContentadd = (EditText)findViewById(R.id.etContent);

        Intent intent = new Intent(this.getIntent());

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ivImage = (ImageView) findViewById(R.id.iv);

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
                mProgressDialog = ProgressDialog.show(PostWriteActivity.this,"",
                        "잠시만 기다려 주세요.",true);
                final String URL =  "http://218.155.147.128:3000/process/contentadd";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(response.equals("1")) {
                                        Intent i = new Intent(PostWriteActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "글을 등록하지 못했습니다.",Toast.LENGTH_SHORT).show();
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
                                mProgressDialog.dismiss();
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

                        if(profileImageBase64 != null) {
                            params.put("image", profileImageBase64);
                        }
                        Log.d(markerlat + " " + markerlong, "lat/lng: ");

                        return params;
                    }
                };

                request.setShouldCache(true);
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                        2000000 ,

                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(this).add(request);

                finish();

                break;

        }
        return true;
    }

    public void onClickSelectImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult  (intent, IMG_REQUEST);
    }

    public void onClickCamera(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                ivImage.setImageBitmap(bitmap);
                ivImage.setVisibility(View.VISIBLE);
                Log.d("btmp", bitmap.toString());
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                byte[] image = outStream.toByteArray();
                profileImageBase64 = Base64.encodeToString(image, 0);
                //send();
            } catch (IOException e) {
                e.printStackTrace();
            }catch(OutOfMemoryError e){
                ivImage.setImageBitmap(bitmap);
                ivImage.setVisibility(View.VISIBLE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
                byte[] image = outStream.toByteArray();
                profileImageBase64 = Base64.encodeToString(image, 0);
                //send();


            }

        }else if(requestCode==1){
            try{

                profileBitmap = (Bitmap)data.getExtras().get("data");
                ivImage.setImageBitmap(profileBitmap );
                ivImage.setVisibility(View.VISIBLE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                byte[] image = outStream.toByteArray();
                profileImageBase64 = Base64.encodeToString(image, 0);
                //send();


            } catch(Exception e){


            }catch(OutOfMemoryError e){
                profileBitmap = (Bitmap)data.getExtras().get("data");
                ivImage.setImageBitmap(profileBitmap);
                ivImage.setVisibility(View.VISIBLE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
                byte[] image = outStream.toByteArray();
                profileImageBase64 = Base64.encodeToString(image, 0);
                //send();


            }
        }

    }

}