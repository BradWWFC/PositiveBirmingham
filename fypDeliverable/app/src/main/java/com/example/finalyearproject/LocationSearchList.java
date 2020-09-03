package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finalyearproject.Adapters.LocationSearchListAdapter;
import com.example.finalyearproject.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocationSearchList extends AppCompatActivity {

    LocationSearchListAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private List<Location> LocationList = new ArrayList<>();
    private List<Location> LocationListAll = new ArrayList<>();
    private boolean sortA_Z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search_list);

        sortA_Z = false;

        Intent intent = getIntent();

        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        adapter = new LocationSearchListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Location gw = keyNode.getValue(Location.class);
                    LocationList.add(gw);

                }
                LocationListAll = LocationList;
                adapter.setLocations(LocationList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        MenuItem sortBy = menu.findItem(R.id.sort);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery() == "") {
                    adapter.setLocations(LocationListAll);
                } else {
                    LocationList.clear();

                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> keys = new ArrayList<>();
                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                String databaseLocationName = keyNode.getValue(Location.class).getNAME();
                                if (databaseLocationName.toLowerCase().contains(searchView.getQuery())) {
                                    keys.add(keyNode.getKey());
                                    Location gw = keyNode.getValue(Location.class);
                                    LocationList.add(gw);

                                }
                            }
                            adapter.setLocations(LocationList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mDatabase.addValueEventListener(postListener);
                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                if (sortA_Z == false) {
                    sortA_Z = true;
                    Collections.sort(LocationList, new Comparator<Location>() {
                        @Override
                        public int compare(Location item1, Location item2) {
                            return item1.getNAME().compareToIgnoreCase(item2.getNAME());
                        }
                    });
                }else{
                    sortA_Z = false;
                    Collections.sort(LocationList, new Comparator<Location>() {
                        @Override
                        public int compare(Location item1, Location item2) {
                            return item2.getNAME().compareToIgnoreCase(item1.getNAME());
                        }
                    });
                }
                adapter.setLocations(LocationList);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
