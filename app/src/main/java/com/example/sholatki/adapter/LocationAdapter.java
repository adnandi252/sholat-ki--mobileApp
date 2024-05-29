package com.example.sholatki.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sholatki.R;
import com.example.sholatki.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{
    private List<Location> locationList;
    private List<Location> locationListFiltered;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
        this.locationListFiltered = new ArrayList<>(locationList);
    }

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
        Location city = locationListFiltered.get(position);
        holder.cityLocation.setText(city.getLokasi());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationListFiltered.size();
    }

    public void filter(String query) {
        Log.d("LocationAdapter", "Filtering with query: " + query);
        locationListFiltered.clear();
        if (query.isEmpty()) {
            locationListFiltered.addAll(locationList);
        } else {
            for (Location city : locationList) {
                if (city.getLokasi().toLowerCase().contains(query.toLowerCase())) {
                    locationListFiltered.add(city);
                    Log.d("LocationAdapter", "City matched: " + city.getLokasi());
                }
            }
        }
        Log.d("LocationAdapter", "Filtered list size: " + locationListFiltered.size());
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView cityLocation;
        CardView itemLocation;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            cityLocation = itemView.findViewById(R.id.cityLocation);
            itemLocation = itemView.findViewById(R.id.item_location);
        }
    }
}
