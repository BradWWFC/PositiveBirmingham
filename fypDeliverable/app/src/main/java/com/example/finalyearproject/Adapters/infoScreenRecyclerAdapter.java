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
import com.example.finalyearproject.model.Location;

import java.util.List;

public class infoScreenRecyclerAdapter extends RecyclerView.Adapter<infoScreenRecyclerAdapter.MyViewHolder> {
    private final LayoutInflater mInflater;
    private List<Location> mDataset;
    private Context contextGlobal;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        ImageView locationImage;
        TextView locationName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            locationImage = itemView.findViewById(R.id.locationImage);
            locationName = itemView.findViewById(R.id.locationName);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public infoScreenRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.info_screen_gw_location_menuitem, parent, false);
        contextGlobal = parent.getContext();
        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mDataset != null) {

            Location current = mDataset.get(position);
            holder.locationName.setText(current.getNAME());
            Glide.with(contextGlobal).load(current.getTHUMBNAIL())
                    .centerCrop().into(holder.locationImage);

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setGuidedWalks(List<Location> locations) {
        mDataset = locations;
        notifyDataSetChanged();
    }
}