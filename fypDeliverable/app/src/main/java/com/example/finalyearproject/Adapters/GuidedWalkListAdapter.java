package com.example.finalyearproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.InfoScreen;
import com.example.finalyearproject.MainActivity;
import com.example.finalyearproject.R;
import com.example.finalyearproject.model.GuidedWalk;
import com.example.finalyearproject.model.GuidedWalkPlaylist;
import com.example.finalyearproject.model.Location;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuidedWalkListAdapter extends RecyclerView.Adapter<GuidedWalkListAdapter.GuidedWalkViewHolder> {

    private final LayoutInflater mInflater;
    private List<GuidedWalk> mGuidedWalks; // Cached copy of guidedWalks
    private List<String> mKeys;
    private Context contextGlobal;
    private ArrayList<Location> locations = new ArrayList<>();
    private GuidedWalk gw;


    public GuidedWalkListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GuidedWalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.guided_walk_menu, parent, false);
        contextGlobal = parent.getContext();
        return new GuidedWalkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GuidedWalkViewHolder holder, int position) {
        if (mGuidedWalks != null) {

            GuidedWalk current = mGuidedWalks.get(position);
            holder.ID = current.getGuidedWalkID();
            holder.guidedWalkTitle.setText(current.getName());
            holder.guidedWalkNumOfLoc.setText("Number of Locations: " + current.getNumberOfLocations());
            Glide.with(contextGlobal).load(current.getThumbnail())
                    .centerCrop().into(holder.guidedWalkImage);


        } else {
            // Covers the case of data not being ready yet.
            holder.guidedWalkTitle.setText("No GuidedWalk");
        }
    }

    public void setGuidedWalks(List<GuidedWalk> guidedWalks) {
        mGuidedWalks = guidedWalks;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mGuidedWalks has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mGuidedWalks != null)
            return mGuidedWalks.size();
        else return 0;
    }

    public GuidedWalk getGuidedWalkAtPosition(int position) {
        return mGuidedWalks.get(position);
    }

    class GuidedWalkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView guidedWalkTitle;
        private final TextView guidedWalkNumOfLoc;
        private final ImageView guidedWalkImage;
        private final Button guidedWalkAddToList;
        private final Button guidedWalkInfo;
        private Context context;
        private long ID;


        private GuidedWalkViewHolder(View itemView) {
            super(itemView);
            guidedWalkTitle = itemView.findViewById(R.id.title);
            guidedWalkNumOfLoc = itemView.findViewById(R.id.numberOfLocations);
            guidedWalkImage = itemView.findViewById(R.id.mainImage);
            guidedWalkAddToList = itemView.findViewById(R.id.addToList);
            guidedWalkInfo = itemView.findViewById(R.id.info);
            context = itemView.getContext();

            guidedWalkAddToList.setOnClickListener(this);
            guidedWalkInfo.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.addToList:
                    Thread thread1 = new Thread() {
                        public void run() {
                            getLocations(ID);
                                                    }
                    };


                    Thread thread2 = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            GuidedWalkPlaylist.addArrayToPlayList(locations);
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    };

                    thread1.start();
                    thread2.start();
                    break;
                case R.id.info:

                    Thread infoThread1 = new Thread() {
                        public void run() {
                            getLocations(ID);
                            getSelectedGuidedWalk(ID);
                        }
                    };


                    Thread infoThread2 = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(context, InfoScreen.class);

                            InfoScreen.setLocationArrayList(locations);
                            InfoScreen.setGw(gw);

                            context.startActivity(intent);
                        }
                    };

                    infoThread1.start();
                    infoThread2.start();


                    break;
            }


        }


    }

    private void getSelectedGuidedWalk(final long id) {

        for (GuidedWalk x : mGuidedWalks) {
            if (x.getGuidedWalkID() == id) {
                gw = x;
            }
        }

    }


    private void getLocations(long ID) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("GuidedWalk/" + ID + "/Locations");

        final DatabaseReference locationsRef = database.getReference("Locations");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                locationsRef.child(Objects.requireNonNull(dataSnapshot.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        Location location = dataSnapshot.getValue(Location.class);
                        locations.add(location);
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(@NotNull DataSnapshot dataSnapshot, String s) {
                //Add the corresponding code for this case
            }

            @Override
            public void onChildRemoved(@NotNull DataSnapshot dataSnapshot) {
                //Add the corresponding code for this case
            }

            @Override
            public void onChildMoved(@NotNull DataSnapshot dataSnapshot, String s) {
                //Add the corresponding code for this case
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }


}
