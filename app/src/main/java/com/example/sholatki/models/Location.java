package com.example.sholatki.models;

public class Location {
    private String id;
    private  String lokasi;

    public Location(String id, String lokasi) {
        this.id = id;
        this.lokasi = lokasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
