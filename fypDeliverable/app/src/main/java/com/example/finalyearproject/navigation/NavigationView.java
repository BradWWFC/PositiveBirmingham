package com.example.finalyearproject.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalyearproject.R;

public class NavigationView extends AppCompatActivity {


    private static final int CAMERA_ANIMATION_DURATION = 1000;
    private static final int DEFAULT_CAMERA_ZOOM = 16;
    private static final int CHANGE_SETTING_REQUEST_CODE = 1;
    private static final int INITIAL_ZOOM = 16;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500;


    private com.mapbox.services.android.navigation.ui.v5.NavigationView navigationView;
    private com.mapbox.services.android.navigation.ui.v5.NavigationView nv;
    private NavigationView nView;
    private NavigationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction();
        navigationView = findViewById(R.id.navigationView);
        fragment = new NavigationFragment(this, getApplicationContext());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fooContainer, fragment)
                .commit();


    }

    @Override
    public void onBackPressed() {
        fragment.onCancelNavigation();
        this.finish();
    }


}
