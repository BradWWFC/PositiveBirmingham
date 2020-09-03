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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.finalyearproject.R;
import com.example.finalyearproject.model.Location;

import java.util.ArrayList;

public class HorizontalLocationListAdapter extends RecyclerView.Adapter<HorizontalLocationListAdapter.ViewHolder> {

    private final ArrayList<Location> destinations;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Location location;

    // data is passed into the constructor
    public HorizontalLocationListAdapter(Context context, ArrayList<Location> destinations) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.destinations = destinations;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.destination_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (destinations!=null) {
            location = destinations.get(position);
            String animal = destinations.get(position).getNAME();
            Glide.with(context).load(destinations.get(position).getTHUMBNAIL())
                    .thumbnail(0.6f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.locationThumbnail);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public void removeItem(final int position) {
        destinations.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, destinations.size());

    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView locationThumbnail;
        TextView myTextView;



        ViewHolder(View itemView) {
            super(itemView);
            locationThumbnail = itemView.findViewById(R.id.locationThumbnail);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            removeItem(getAdapterPosition());
            return true;
        }

    }

    // convenience method for getting data at click position
    public Location getItem(int id) {
        return destinations.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
