package com.example.sholatki.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sholatki.ApiClient;
import com.example.sholatki.ApiServices;
import com.example.sholatki.DatabaseHelper;
import com.example.sholatki.R;
import com.example.sholatki.responses.HadithResponse;
import com.example.sholatki.responses.PrayerScheduleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private TextView tv_countdownTimer, tv_nextPrayerTime, tv_judul, tv_hadith, tv_kotak_ilmu;
    private CountDownTimer countDownTimer;
    private String[] prayerTimes;
    private String[] prayerNames = {"Subuh", "Dhuha", "Dzuhur", "Ashar", "Maghrib", "Isya"};
    private int nextPrayerIndex;
    private Handler handler;
    ProgressBar progres_hadits;
    ImageButton btn_refresh, btn_bookmark;
    String perawi, arabicHadith, indonesianHadith;
    int number;
    DatabaseHelper databaseHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tv_countdownTimer = view.findViewById(R.id.countdown_timer);
        tv_nextPrayerTime = view.findViewById(R.id.next_prayer_time);
        tv_judul = view.findViewById(R.id.tv_judul);
        tv_hadith = view.findViewById(R.id.tv_hadith);
        tv_kotak_ilmu = view.findViewById(R.id.tv_kotak_ilmu);
        progres_hadits = view.findViewById(R.id.progres_hadits);
        btn_refresh = view.findViewById(R.id.btn_refresh);
        btn_bookmark = view.findViewById(R.id.btn_bookmark);

        progres_hadits.setVisibility(View.VISIBLE);

        databaseHelper = new DatabaseHelper(getContext());

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchHadithData();
            }
        });

        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!databaseHelper.isHadisAlreadyBookmarked(perawi, number+"")) {
                    databaseHelper.addHadis(perawi, number+"", arabicHadith, indonesianHadith);
                } else {
                    Toast.makeText(getContext(), "Hadis sudah ada dalam daftar bookmark", Toast.LENGTH_SHORT).show();
                }
            }
        });

        handler = new Handler();

        loadPrayerTimes();
        calculateNextPrayerTime();

        fetchHadithData();
        return view;
    }

    private void loadPrayerTimes() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        prayerTimes = new String[]{
                prefs.getString("subuh", "04:23:00"),
                prefs.getString("dhuha", "06:10:00"),
                prefs.getString("dzuhur", "11:38:00"),
                prefs.getString("ashar", "14:57:00"),
                prefs.getString("maghrib", "17:28:00"),
                prefs.getString("isya", "18:42:00")
        };

        // Log the loaded prayer times for debugging
        for (int i = 0; i < prayerTimes.length; i++) {
            Log.d("HomeFragment", "Loaded prayer time for " + prayerNames[i] + ": " + prayerTimes[i]);
        }
    }

    private void calculateNextPrayerTime() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        for (int i = 0; i < prayerTimes.length; i++) {
            try {
                Date prayerTime = timeFormat.parse(prayerTimes[i]);
                Calendar prayerCalendar = Calendar.getInstance();
                prayerCalendar.setTime(prayerTime);

                // Set prayerCalendar to today's date
                prayerCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
                prayerCalendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
                prayerCalendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

                if (prayerCalendar.after(now)) {
                    nextPrayerIndex = i;
                    long timeDifference = prayerCalendar.getTimeInMillis() - now.getTimeInMillis();
                    Log.d("HomeFragment", "Next prayer is " + prayerNames[nextPrayerIndex] + " in " + timeDifference + " ms");

                    startCountDownTimer(timeDifference);
                    return;
                }
            } catch (ParseException e) {
                Log.e("HomeFragment", "Error parsing prayer time: " + e.getMessage());
            }
        }

        // Handle case when no prayer time is after the current time
        nextPrayerIndex = 0;
        try {
            Date prayerTime = timeFormat.parse(prayerTimes[0]);
            Calendar nextDayPrayerCalendar = Calendar.getInstance();
            nextDayPrayerCalendar.setTime(prayerTime);
            nextDayPrayerCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
            nextDayPrayerCalendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
            nextDayPrayerCalendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1); // Add 1 day

            long timeDifference = nextDayPrayerCalendar.getTimeInMillis() - now.getTimeInMillis();
            Log.d("HomeFragment", "Next prayer is " + prayerNames[nextPrayerIndex] + " tomorrow in " + timeDifference + " ms");

            startCountDownTimer(timeDifference);
        } catch (ParseException e) {
            Log.e("HomeFragment", "Error parsing prayer time: " + e.getMessage());
        }
    }

    private void startCountDownTimer(long millisUntilFinished) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                Log.d("HomeFragment", "Countdown: " + timeLeftFormatted);

                handler.post(() -> tv_countdownTimer.setText(timeLeftFormatted));
            }

            @Override
            public void onFinish() {
                Log.d("HomeFragment", "Countdown finished");

                handler.post(() -> {
                    tv_countdownTimer.setText("00:00:00");
                    tv_nextPrayerTime.setText("Waktu Sholat " + prayerNames[nextPrayerIndex] + " Telah Tiba");
                    calculateNextPrayerTime();
                });
            }
        };

        countDownTimer.start();
        tv_nextPrayerTime.setText("Menuju Sholat " + prayerNames[nextPrayerIndex]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    void fetchHadithData() {
        ApiServices apiService = ApiClient.getClient().create(ApiServices.class);
        Call<HadithResponse> call = apiService.getRandomPerawiHadith();

        call.enqueue(new Callback<HadithResponse>() {
            @Override
            public void onResponse(Call<HadithResponse> call, Response<HadithResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progres_hadits.setVisibility(View.VISIBLE);
                    HadithResponse hadithResponse = response.body();
                    if (hadithResponse.getInfo() != null && hadithResponse.getInfo().getPerawi() != null) {
                        tv_kotak_ilmu.setText("Kotak Ilmu");
                        perawi = hadithResponse.getInfo().getPerawi().getName();
                        number = hadithResponse.getData().getNumber();
                        arabicHadith = hadithResponse.getData().getArab();
                        indonesianHadith = hadithResponse.getData().getId();
                        tv_judul.setText("Perawi: Imam " + perawi +
                                "\nNomor: " + number);
                        tv_hadith.setText(arabicHadith + "\n\n" + indonesianHadith);
                        progres_hadits.setVisibility(View.GONE);
                    } else {
                        Log.e("HadithData", "Info or Perawi is null");
                        tv_judul.setText("Info or Perawi is null");
                        tv_hadith.setText("");
                    }
                } else {
                    Log.e("HadithData", "Response was not successful or body is null");
                    tv_judul.setText("Failed to fetch hadith data");
                    tv_hadith.setText("");
                }
            }

            @Override
            public void onFailure(Call<HadithResponse> call, Throwable t) {
                Log.e("HadithData", "Failed to fetch hadith data: ", t);
                tv_judul.setText("Failed to fetch hadith data");
                tv_hadith.setText("");
            }
        });
    }
}