package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.Adapters.GuidedWalkListAdapter;
import com.example.finalyearproject.model.GuidedWalk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuidedWalkList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<GuidedWalk> guidedWalkList = new ArrayList<>();
    private List<GuidedWalk> guidedWalkListAll = new ArrayList<>();
    private DatabaseReference mDatabase;
    private GuidedWalkListAdapter adapter;
    private boolean sortA_Z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guided_walk_list);

        sortA_Z = false;
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference("GuidedWalk");

        Intent intent = getIntent();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewGW);
        adapter = new GuidedWalkListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    GuidedWalk gw = keyNode.getValue(GuidedWalk.class);
                    guidedWalkList.add(gw);

                }
                // ...
                adapter.setGuidedWalks(guidedWalkList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery() == "") {
                    adapter.setGuidedWalks(guidedWalkListAll);
                } else {
                    guidedWalkList.clear();

                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> keys = new ArrayList<>();
                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                String databaseLocationName = keyNode.getValue(GuidedWalk.class).getName();
                                if (databaseLocationName.toLowerCase().contains(searchView.getQuery())) {
                                    keys.add(keyNode.getKey());
                                    GuidedWalk gw = keyNode.getValue(GuidedWalk.class);
                                    guidedWalkList.add(gw);

                                }
                            }
                            adapter.setGuidedWalks(guidedWalkList);

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
                    Collections.sort(guidedWalkList, new Comparator<GuidedWalk>() {
                        @Override
                        public int compare(GuidedWalk item1, GuidedWalk item2) {
                            return item1.getName().compareToIgnoreCase(item2.getName());
                        }
                    });
                } else {
                    sortA_Z = false;
                    Collections.sort(guidedWalkList, new Comparator<GuidedWalk>() {
                        @Override
                        public int compare(GuidedWalk item1, GuidedWalk item2) {
                            return item2.getName().compareToIgnoreCase(item1.getName());
                        }
                    });
                }
                adapter.setGuidedWalks(guidedWalkList);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
