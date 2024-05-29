package com.example.sholatki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.sholatki.models.Hadith;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hadis_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HADIS = "hadis";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PERAWI = "perawi";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_ARABIC_HADITH = "arabic_hadith";
    private static final String COLUMN_INDONESIAN_HADITH = "indonesian_hadith";
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HADIS = "CREATE TABLE " + TABLE_HADIS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PERAWI + " TEXT," +
                COLUMN_NUMBER + " TEXT," +
                COLUMN_ARABIC_HADITH + " TEXT," +
                COLUMN_INDONESIAN_HADITH + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_HADIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HADIS);
        onCreate(db);
    }

    public void addHadis(String perawi, String number, String arabicHadith, String indonesianHadith) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERAWI, perawi);
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_ARABIC_HADITH, arabicHadith);
        values.put(COLUMN_INDONESIAN_HADITH, indonesianHadith);
        db.insert(TABLE_HADIS, null, values);
        db.close();
    }

    public boolean isHadisAlreadyBookmarked(String perawi, String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HADIS +
                " WHERE " + COLUMN_PERAWI + " = ? AND " +
                COLUMN_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{perawi, number});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public ArrayList<Hadith> getAllHadith() {
        ArrayList<Hadith> hadithList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HADIS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hadith hadith = new Hadith();
                hadith.setId(cursor.getInt(0));
                hadith.setPerawi(cursor.getString(1));
                hadith.setNumber(cursor.getString(2));
                hadith.setArabicHadith(cursor.getString(3));
                hadith.setIndonesianHadith(cursor.getString(4));
                hadithList.add(hadith);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return hadithList;
    }

    public void deleteHadith(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";

        String[] selectionArgs = { id };

        int deletedRows = db.delete(TABLE_HADIS, selection, selectionArgs);

        if (deletedRows == 0) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
