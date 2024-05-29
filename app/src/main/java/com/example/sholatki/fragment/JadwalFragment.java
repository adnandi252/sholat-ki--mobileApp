package com.example.sholatki.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sholatki.ApiClient;
import com.example.sholatki.ApiServices;
import com.example.sholatki.models.PrayerSchedule;
import com.example.sholatki.responses.PrayerScheduleResponse;
import com.example.sholatki.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalFragment extends Fragment {
    ProgressBar progresSubuh, progresDhuha, progresDzuhur, progresAshar, progresMaghrib, progresIsya, progresLocation;
    TextView subuhTime, dhuhaTime, dzuhurTime, asharTime, maghribTime, isyaTime, tvLocation, tv_date;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jadwal, container, false);
        subuhTime = view.findViewById(R.id.subuh_time);
        dhuhaTime = view.findViewById(R.id.dhuha_time);
        dzuhurTime = view.findViewById(R.id.dzuhur_time);
        asharTime = view.findViewById(R.id.ashar_time);
        maghribTime = view.findViewById(R.id.maghrib_time);
        isyaTime = view.findViewById(R.id.isya_time);
        tv_date = view.findViewById(R.id.date);

        progresSubuh = view.findViewById(R.id.progres_subuh);
        progresDhuha = view.findViewById(R.id.progres_dhuha);
        progresDzuhur = view.findViewById(R.id.progres_dzuhur);
        progresAshar = view.findViewById(R.id.progres_ashar);
        progresMaghrib = view.findViewById(R.id.progres_maghrib);
        progresIsya = view.findViewById(R.id.progres_isya);

        tvLocation = view.findViewById(R.id.tv_location);
        progresLocation = view.findViewById(R.id.progres_location);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String formattedDate = dateFormat.format(date);

        tv_date.setText(formattedDate);

        showLoadingState();

        fetchPrayerSchedule();
        return view;
    }

    private void fetchPrayerSchedule() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String locationId = prefs.getString("location_id", "1632");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String dateText = dateFormat.format(date);

        ApiServices apiService = ApiClient.getClient().create(ApiServices.class);
        Call<PrayerScheduleResponse> call = apiService.getPrayerSchedule(locationId, dateText);

        call.enqueue(new Callback<PrayerScheduleResponse>() {
            @Override
            public void onResponse(Call<PrayerScheduleResponse> call, Response<PrayerScheduleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PrayerScheduleResponse prayerScheduleResponse = response.body();
                    PrayerSchedule schedule = prayerScheduleResponse.getData().getJadwal();

                    subuhTime.setText(schedule.getSubuh());
                    dhuhaTime.setText(schedule.getDhuha());
                    dzuhurTime.setText(schedule.getDzuhur());
                    asharTime.setText(schedule.getAshar());
                    maghribTime.setText(schedule.getMaghrib());
                    isyaTime.setText(schedule.getIsya());
                    tvLocation.setText(toCamelCase(prayerScheduleResponse.getData().getLokasi() + ", Prov. " + prayerScheduleResponse.getData().getDaerah()));

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("subuh", schedule.getSubuh()+":00");
                    editor.putString("dhuha", schedule.getDhuha()+":00");
                    editor.putString("dzuhur", schedule.getDzuhur()+":00");
                    editor.putString("ashar", schedule.getAshar()+":00");
                    editor.putString("maghrib", schedule.getMaghrib()+":00");
                    editor.putString("isya", schedule.getIsya()+":00");
                    editor.apply();

                    showContentState();

                    Log.d("JadwalFragment", "Prayer schedule fetched successfully");
                } else {
                    Log.d("JadwalFragment", "Failed to fetch prayer schedule: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PrayerScheduleResponse> call, Throwable t) {
                Log.e("JadwalFragment", "Error fetching prayer schedule", t);
            }
        });
    }

    private void showLoadingState() {
        progresSubuh.setVisibility(View.VISIBLE);
        progresDhuha.setVisibility(View.VISIBLE);
        progresDzuhur.setVisibility(View.VISIBLE);
        progresAshar.setVisibility(View.VISIBLE);
        progresMaghrib.setVisibility(View.VISIBLE);
        progresIsya.setVisibility(View.VISIBLE);
        progresLocation.setVisibility(View.VISIBLE);

        subuhTime.setVisibility(View.GONE);
        dhuhaTime.setVisibility(View.GONE);
        dzuhurTime.setVisibility(View.GONE);
        asharTime.setVisibility(View.GONE);
        maghribTime.setVisibility(View.GONE);
        isyaTime.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
    }

    private void showContentState() {
        progresSubuh.setVisibility(View.GONE);
        progresDhuha.setVisibility(View.GONE);
        progresDzuhur.setVisibility(View.GONE);
        progresAshar.setVisibility(View.GONE);
        progresMaghrib.setVisibility(View.GONE);
        progresIsya.setVisibility(View.GONE);
        progresLocation.setVisibility(View.GONE);

        subuhTime.setVisibility(View.VISIBLE);
        dhuhaTime.setVisibility(View.VISIBLE);
        dzuhurTime.setVisibility(View.VISIBLE);
        asharTime.setVisibility(View.VISIBLE);
        maghribTime.setVisibility(View.VISIBLE);
        isyaTime.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
    }

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                c = Character.toUpperCase(c);
                capitalizeNext = false;
            } else {
                c = Character.toLowerCase(c);
            }
            result.append(c);
        }

        return result.toString();
    }
}