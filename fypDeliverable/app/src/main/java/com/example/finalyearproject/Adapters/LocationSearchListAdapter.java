package com.example.finalyearproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.MainActivity;
import com.example.finalyearproject.R;
import com.example.finalyearproject.jSoup;
import com.example.finalyearproject.model.GuidedWalkPlaylist;
import com.example.finalyearproject.model.Location;
import com.example.finalyearproject.pager.TabbedInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LocationSearchListAdapter extends RecyclerView.Adapter<LocationSearchListAdapter.LocationSearchViewHolder> {

    private final LayoutInflater mInflater;
    private Context contextGlobal;
    private List<Location> mLocations;
    private List<Location> mLocationsAll;
    private Context mContext;

    public LocationSearchListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LocationSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.location_menu_item, parent, false);
        contextGlobal = parent.getContext();
        return new LocationSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationSearchViewHolder holder, int position) {
        if (mLocations != null) {
            Location current = mLocations.get(position);
            holder.location = current;
            holder.locationTitle.setText(current.getNAME());
            holder.locationID = current.getLocationID();
            Glide.with(contextGlobal).load(current.getTHUMBNAIL())
                    .centerCrop().into(holder.locationThumbnail);
        }
    }

    @Override
    public int getItemCount() {
            if (mLocations != null)
                return mLocations.size();
            else return 0;
    }

    public void setAllLocations(List<Location> locations) {
        mLocationsAll = locations;
    }

    public void setLocations(List<Location> locations) {
        mLocations = locations;
        notifyDataSetChanged();
    }

    public class LocationSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Location location;
        private final TextView locationTitle;
        private final CardView locationButton;
        private final ImageView locationThumbnail;
        private final Button addToList;
        private final Button info;
        private long locationID;
        private Context context;

        public LocationSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.title);
            locationButton = itemView.findViewById(R.id.card_view);
            locationThumbnail = itemView.findViewById(R.id.mainImage);
            addToList = itemView.findViewById(R.id.addToList);
            info = itemView.findViewById(R.id.info);
            context = itemView.getContext();


            addToList.setOnClickListener(this);
            info.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.addToList:
                    Thread thread1 = new Thread() {
                        public void run(){
                            addLocation();
                        }
                    };

                    Thread thread2 = new Thread() {
                        public void run(){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);

                        }
                    };

                    thread1.start();
                    thread2.start();
                    break;
                case R.id.info:
                    Thread Infothread1 = new Thread() {
                        public void run() {

                            String hyperlink = location.getHYPERLINK();
                            new jSoup(hyperlink).execute();
                        }
                    };

                    Thread Infothread2 = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                Intent intent = new Intent(mContext, TabbedInfo.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("thumbnailURL", location.getTHUMBNAIL());
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    Infothread1.start();
                    Infothread2.start();
                    break;
            }



        }

        public void addLocation(){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Locations");

            mDatabase.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        long databaseLocationID = data.getValue(Location.class).getLocationID();
                        if(databaseLocationID==locationID){
                             GuidedWalkPlaylist.addWalkLocationsItem(data.getValue(Location.class));
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }

            });
        }
    }
}
