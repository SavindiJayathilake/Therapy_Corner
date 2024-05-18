package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.os.Environment;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "user_database.db";
    public static final String DB_TABLE = "NotesTable";

    public static String COLUMN_ID = "NotesId";
    public static String COLUMN_TITLE = "NotesTitle";
    public static String COLUMN_DETAILS = "NotesDetails";
    public static String COLUMN_DATE = "NotesDate";
    public static String COLUMN_TIME = "NotesTime";

    public static String COLUMN_USERNAME = "NotesUsername";




    public DatabaseHelper(@Nullable Context context) { super(context, DB_NAME, null, DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + DB_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES users(username))";

        sqLiteDatabase.execSQL(query);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i > i1)
            return;
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(sqLiteDatabase);

    }

    public NoteModel getNotes(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] query = new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_DETAILS, COLUMN_DATE, COLUMN_TIME, COLUMN_USERNAME};
        Cursor cursor = db.query(DB_TABLE, query, COLUMN_ID+"=?", new String[] {String.valueOf(id)},null ,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        return new NoteModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
    }

    void deleteNote(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(DB_TABLE, COLUMN_ID+ "=?", new String[]{String.valueOf(id)});
        db.close();
    }

}