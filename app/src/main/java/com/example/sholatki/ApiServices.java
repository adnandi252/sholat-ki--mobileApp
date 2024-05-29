package com.example.sholatki;

import com.example.sholatki.responses.HadithResponse;
import com.example.sholatki.responses.LocationResponse;
import com.example.sholatki.responses.PrayerScheduleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServices {
    @GET("sholat/kota/semua")
    Call<LocationResponse> getAllLocations();

    @GET("sholat/jadwal/{idKota}/{date}")
    Call<PrayerScheduleResponse> getPrayerSchedule(@Path("idKota") String idKota, @Path("date") String date);

    @GET("hadits/perawi/acak")
    Call<HadithResponse> getRandomPerawiHadith();
}
