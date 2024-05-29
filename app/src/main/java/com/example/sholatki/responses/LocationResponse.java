package com.example.sholatki.responses;

import com.example.sholatki.models.Location;

import java.util.List;

public class LocationResponse {
    private List<Location> data;

    public List<Location> getData() {
        return data;
    }

    public void setData(List<Location> data) {
        this.data = data;
    }
}
