package com.example.sholatki;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sholatki.adapter.HadithAdapter;
import com.example.sholatki.models.Hadith;

import java.util.ArrayList;

public class HadithActivity extends AppCompatActivity {
    private RecyclerView rv_hadith;
    private HadithAdapter adapter;
    private ArrayList<Hadith> hadithList;
    private DatabaseHelper databaseHelper;
    Toolbar toolbar_hadith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadith);

        rv_hadith = findViewById(R.id.rv_hadith);
        rv_hadith.setHasFixedSize(true);
        rv_hadith.setLayoutManager(new LinearLayoutManager(this));

        toolbar_hadith = findViewById(R.id.toolbar_hadith);
        setSupportActionBar(toolbar_hadith);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        hadithList = new ArrayList<>();

        hadithList = databaseHelper.getAllHadith();

        adapter = new HadithAdapter(this, hadithList);
        rv_hadith.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}