package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.Adapters.infoScreenRecyclerAdapter;
import com.example.finalyearproject.model.GuidedWalk;
import com.example.finalyearproject.model.Location;

import java.util.ArrayList;

public class InfoScreen extends AppCompatActivity {

    private static GuidedWalk gw;
    private static ArrayList<Location> locationArrayList;
    private RecyclerView recyclerView;
    private infoScreenRecyclerAdapter adapter;
    private ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);
        recyclerView = (RecyclerView) findViewById(R.id.locationRecyclerView);
        backdrop = findViewById(R.id.backdrop);

        TextView description = findViewById(R.id.description);
        description.setText(gw.getDescription());

        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        adapter = new infoScreenRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setGuidedWalks(locationArrayList);

        Glide.with(getApplicationContext()).load(gw.getThumbnail()).into(backdrop);

    }

    public static void setLocationArrayList(ArrayList<Location> x) {
        locationArrayList = x;
    }

    public static void setGw(GuidedWalk x) {
        gw = x;
    }
}
