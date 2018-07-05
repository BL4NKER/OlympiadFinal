package com.blanksoft.olympiadfinal1;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Fragment fragment;

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
                    break;
                case R.id.navigation_dashboard:
                    fragment = new PinDropFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new ProfileFragment();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                startActivity(i);
                break;

        }
        return true;
    }
}
