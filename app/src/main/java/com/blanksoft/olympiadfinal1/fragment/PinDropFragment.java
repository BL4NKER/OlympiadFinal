package com.blanksoft.olympiadfinal1.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.activity.CompleteWriteActivity;
import com.blanksoft.olympiadfinal1.activity.PostDetailActivity;
import com.blanksoft.olympiadfinal1.model.Content;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.sql.DriverManager.println;


public class PinDropFragment extends SupportMapFragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;



    Content content2;

    MarkerOptions marker;

    private Double mLat;
    private Double mLong;

    private String name;
    private String content;
    private String contentId;

    @Override
    public void onStart(){
        super.onStart();
        if(this.mGoogleApiClient != null){
            this.mGoogleApiClient.connect();
        }
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLat = getArguments().getDouble("mLat");
            mLong = getArguments().getDouble("mLong");
        }

    }
    private void setGoMarker() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLat, mLong)));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }
    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        //if (mGoogleApiClient != null) {
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapFrag.onLowMemory();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mGoogleMap.clear();
        mapFrag = (SupportMapFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(mapFrag).commit();
    }
    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        ProgressDialog mProgressDialog;
        mProgressDialog= new ProgressDialog(getActivity());
        mProgressDialog.setMessage("잠시만 기다려 주세요.");
        mProgressDialog.show();
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMarkerClickListener(this);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                if (getArguments() != null) {
                    setGoMarker();
                } else {
                    buildGoogleApiClient();
                    mGoogleMap.setMyLocationEnabled(true);
                }
                mProgressDialog.dismiss();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            if (getArguments() != null) {
                setGoMarker();
            } else {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
            mProgressDialog.dismiss();
        }

        final String URL =  "http://218.155.147.128:3000/content";

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            JSONArray jsonarray = new JSONArray(response);

                            println("onResponse() 호출됨 : " + response);
                            for(int i =0; i<response.length(); i++){

                                JSONObject jresponse = response.getJSONObject(i);
                                if (String.valueOf(jresponse.optBoolean("Bcomplete")).equals("false")) {
                                    double lat, lng;

                                    content2 = new Content(jresponse.optDouble("makerlat"), jresponse.optDouble("makerlong"));

                                    Log.d("qwt", content2.toString());

                                    marker = new MarkerOptions().position(
                                            new LatLng(content2.getLat(), content2.getLng()));

                                    // String log = String.valueOf(lat);

                                    //rgbg nLog.d("제발나와래이", log);

                                    googleMap.addMarker(marker);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        //optionally, stop location updates if only current location is needed
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final String m1 = String.valueOf(marker.getPosition().latitude);
        final String m2 = String.valueOf(marker.getPosition().longitude);
        Log.d("qwtqwt","qwtqt");

        String url =  "http://218.155.147.128:3000/findByGps";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jaresponse = new JSONArray(response);
                            JSONObject jresponse = jaresponse.getJSONObject(0);
                            name = jresponse.optString("name");
                            content = jresponse.optString("content");
                            contentId = jresponse.optString("contentid");

                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.item_dialog_gps, null);
                            TextView tvName = alertLayout.findViewById(R.id.tvName);
                            TextView tvContent = alertLayout.findViewById(R.id.tvContent);
                            tvName.setText(name);
                            tvContent.setText(content);

                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            builder.setTitle("퀘스트 내용")
                                    .setView(alertLayout)
                                    //builder.setMessage("Look at this dialog!")
                                    .setCancelable(false)
                                    .setNegativeButton("취소",null)
                                    .setNeutralButton("자세히 보기", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialo, int id) {
                                            Intent intent = new Intent(getContext(), PostDetailActivity.class);
                                            intent.putExtra("contentid", contentId);
                                            startActivity(intent);
                                        }
                                    })
                                    .setPositiveButton("완료하기", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getContext(), CompleteWriteActivity.class);
                                            intent.putExtra("registUser", name);
                                            intent.putExtra("contentId", contentId);
                                            startActivity(intent);
                                        }
                                    });
                            android.support.v7.app.AlertDialog alert = builder.create();
                            alert.show();

                            Log.d(name + " " + content, "onResponse: ");

                            //Log.d("ggw",jresponse.getString("content"));
                            println("onResponse() 호출됨 : " + response);
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
                Map<String, String>  params = new HashMap<>();

                //Log.d(m, "getParams: ");

                //for (int i = 0 ; i<markerlat.length ; i++) {
                //if (markerlat[i].equals(m1)) {
                params.put("lat", m1);
                params.put("long", m2);
                //}
                //}

                return params;
            }
        };

        request.setShouldCache(true);
        Volley.newRequestQueue(getActivity()).add(request);

        println("웹서버에 요청함 : " + url);

        return true;
    }

}