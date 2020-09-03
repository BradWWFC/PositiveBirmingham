package com.example.finalyearproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.R;
import com.example.finalyearproject.model.GuidedWalk;
import com.example.finalyearproject.model.Location;

import java.util.ArrayList;
import java.util.List;

public class GuidedWalkLaunchListAdapter extends RecyclerView.Adapter<GuidedWalkLaunchListAdapter.GuidedWalkLaunchViewHolder> {

        private final LayoutInflater mInflater;
        private ArrayList<Location> mLocation; // Cached copy of guidedWalks
        private List<String> mKeys;
        private Context contextGlobal;
        private ArrayList<Location> locations = new ArrayList<>();
        private GuidedWalk gw;


    public GuidedWalkLaunchListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public GuidedWalkLaunchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.info_screen_gw_location_menuitem, parent, false);
            contextGlobal = parent.getContext();
            return new GuidedWalkLaunchViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GuidedWalkLaunchViewHolder holder, int position) {
            if (mLocation != null) {
                Location current = mLocation.get(position);
               holder.locationName.setText(current.getNAME());
                Glide.with(contextGlobal).load(current.getTHUMBNAIL())
                        .centerCrop().into(holder.locationImage);


            } else {
                // Covers the case of data not being ready yet.
            }
        }

        public void setGuidedWalks(ArrayList<Location> guidedWalks) {
            mLocation = guidedWalks;
            notifyDataSetChanged();
        }

        // getItemCount() is called many times, and when it is first called,
        // mGuidedWalks has not been updated (means initially, it's null, and we can't return null).
        @Override
        public int getItemCount() {
            if (mLocation != null)
                return mLocation.size();
            else return 0;
        }

        public Location getLocationAtPosition(int position) {
            return mLocation.get(position);
        }

        class GuidedWalkLaunchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private final TextView locationName;
            private final ImageView locationImage;

            public GuidedWalkLaunchViewHolder(@NonNull View itemView) {
                super(itemView);

                locationImage = itemView.findViewById(R.id.locationImage);
                locationName = itemView.findViewById(R.id.locationName);
            }

            @Override
            public void onClick(View v) {

            }
        }
}
