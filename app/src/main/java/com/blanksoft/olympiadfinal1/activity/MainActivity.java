package com.blanksoft.olympiadfinal1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.blanksoft.olympiadfinal1.tool.BackPressCloseHandler;
import com.blanksoft.olympiadfinal1.fragment.PinDropFragment;
import com.blanksoft.olympiadfinal1.fragment.ProfileFragment;
import com.blanksoft.olympiadfinal1.R;
import com.blanksoft.olympiadfinal1.fragment.RankingFragment;
import com.blanksoft.olympiadfinal1.TimeLineFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Fragment fragment;
    BottomNavigationViewEx navigationViewEx;
    private BackPressCloseHandler backPressCloseHandler;

    private Handler mHandler = new Handler();

    FragmentManager fragmentManager = getSupportFragmentManager();
    //FragmentTransaction transaction = fragmentManager.beginTransaction();
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new TimeLineFragment();
                    navigationViewEx.getMenu().getItem(1).setEnabled(false);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            navigationViewEx.getMenu().getItem(1).setEnabled(true);
                        }
                    }, 2000);
                    break;
                case R.id.navigation_dashboard:
                    fragment = new PinDropFragment();
                    navigationViewEx.getMenu().getItem(1).setEnabled(false);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            navigationViewEx.getMenu().getItem(1).setEnabled(true);
                        }
                    }, 2000);
                    break;
                case R.id.navigation_notifications:
                    fragment = new RankingFragment();
                    navigationViewEx.getMenu().getItem(1).setEnabled(false);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            navigationViewEx.getMenu().getItem(1).setEnabled(true);
                        }
                    }, 2000);
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    navigationViewEx.getMenu().getItem(1).setEnabled(false);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            navigationViewEx.getMenu().getItem(1).setEnabled(true);
                        }
                    }, 2000);
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment).commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new TimeLineFragment()).commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        navigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigationViewEx.enableAnimation(false);
        navigationViewEx.enableShiftingMode(false);
        navigationViewEx.enableItemShiftingMode(false);
        navigationViewEx.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_post:
                Toast.makeText(this, "1111",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, PostGpsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

        }
        return true;
    }
}