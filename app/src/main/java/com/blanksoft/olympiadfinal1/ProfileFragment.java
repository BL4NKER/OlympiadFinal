package com.blanksoft.olympiadfinal1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {

    Button btHistory;
    Button btLikePost;
    Button btLogout;

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

        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        btLikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
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
}
