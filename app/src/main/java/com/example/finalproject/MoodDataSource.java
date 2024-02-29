package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class MoodDataSource {

    private SQLiteDatabase database;
    private MoodDatabaseHelper dbHelper;
    private String[] allColumns = {MoodDatabaseHelper.COLUMN_MOOD_ID,
            MoodDatabaseHelper.COLUMN_MOOD_DATE,
            MoodDatabaseHelper.COLUMN_MOOD_MOOD,
            MoodDatabaseHelper.COLUMN_MOOD_REASON,
            MoodDatabaseHelper.COLUMN_USERNAME};

    public MoodDataSource(Context context) {
        dbHelper = new MoodDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertMood(MoodEntry moodEntry, String username) {
        ContentValues values = new ContentValues();
        values.put(MoodDatabaseHelper.COLUMN_MOOD_DATE, moodEntry.getDate());
        values.put(MoodDatabaseHelper.COLUMN_MOOD_MOOD, moodEntry.getMood());
        values.put(MoodDatabaseHelper.COLUMN_MOOD_REASON, moodEntry.getReason());
        values.put(MoodDatabaseHelper.COLUMN_USERNAME, username);

        long insertId = database.insert(MoodDatabaseHelper.TABLE_MOODS, null, values);
        moodEntry.setId(insertId);
    }

    public List<MoodEntry> getAllMoods() {
        List<MoodEntry> moods = new ArrayList<>();

        Cursor cursor = database.query(MoodDatabaseHelper.TABLE_MOODS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MoodEntry moodEntry = cursorToMood(cursor);
            moods.add(moodEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return moods;
    }

    public List<MoodEntry> getMoodsForDateAndUsername(String date, String username) {
        List<MoodEntry> moods = new ArrayList<>();

        Cursor cursor = database.query(MoodDatabaseHelper.TABLE_MOODS,
                allColumns,
                MoodDatabaseHelper.COLUMN_MOOD_DATE + " = ? AND " + MoodDatabaseHelper.COLUMN_USERNAME + " = ?",
                new String[]{date, username},
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MoodEntry moodEntry = cursorToMood(cursor);
            moods.add(moodEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return moods;
    }

    private MoodEntry cursorToMood(Cursor cursor) {
        MoodEntry moodEntry = new MoodEntry();
        moodEntry.setId(cursor.getLong(0));
        moodEntry.setDate(cursor.getString(1));
        moodEntry.setMood(cursor.getString(2));
        moodEntry.setReason(cursor.getString(3));
        return moodEntry;
    }
}
