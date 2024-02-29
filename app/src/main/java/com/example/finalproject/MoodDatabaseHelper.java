package com.example.finalproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mood_user_database.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MOODS = "moods";
    public static final String COLUMN_MOOD_ID = "_id";
    public static final String COLUMN_MOOD_DATE = "date";
    public static final String COLUMN_MOOD_MOOD = "mood";
    public static final String COLUMN_MOOD_REASON = "reason";

    public static String COLUMN_USERNAME = "moodsUsername";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MOODS + " (" +
                    COLUMN_MOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MOOD_DATE + " TEXT, " +
                    COLUMN_MOOD_MOOD + " TEXT, " +
                    COLUMN_MOOD_REASON + " TEXT," +
                    COLUMN_USERNAME + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES users(username))";

    public MoodDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOODS);
        onCreate(db);
    }
}
