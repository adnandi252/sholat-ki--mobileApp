package com.example.sholatki.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sholatki.ApiClient;
import com.example.sholatki.ApiServices;
import com.example.sholatki.models.Location;
import com.example.sholatki.adapter.LocationAdapter;
import com.example.sholatki.responses.LocationResponse;
import com.example.sholatki.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment {
    RecyclerView rv_search;
    LocationAdapter locationAdapter;
    TextInputEditText searchLocation;
    List<Location> locationList;
    SharedPreferences prefs;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);


        rv_search = view.findViewById(R.id.rv_search);
        rv_search.setVisibility(View.GONE);
        searchLocation = view.findViewById(R.id.search_location);

        rv_search.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String locationName = prefs.getString("location_name", null);
        searchLocation.setText(locationName);

        fetchCities();

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (locationAdapter != null) {
                    locationAdapter.filter(s.toString());
                    rv_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void fetchCities() {
        ApiServices apiService = ApiClient.getClient().create(ApiServices.class);
        Call<LocationResponse> call = apiService.getAllLocations();

        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocationResponse locationResponse = response.body();
                    locationList = locationResponse.getData();
                    locationAdapter = new LocationAdapter(getContext(), locationList);
                    rv_search.setAdapter(locationAdapter);

                    locationAdapter.setOnItemClickListener(location -> {
                        searchLocation.setText(location.getLokasi());
                        rv_search.setVisibility(View.GONE);
                        getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
                                .putString("location_id", location.getId())
                                .putString("location_name", location.getLokasi())
                                .apply();
                    });

                } else {
                    Log.d("SettingsFragment", "Failed to fetch cities: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.e("SettingsFragment", "Error fetching cities", t);
            }
        });
    }
}