package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText title, details;
    Button addNoteBtn;
    String todayDate, currentTime;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.addNote);
        details = findViewById(R.id.noteDetails);
        addNoteBtn = findViewById(R.id.addNoteBtn);

        calendar = Calendar.getInstance();
        todayDate = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calendar.get(Calendar.HOUR_OF_DAY))+":"+pad(calendar.get(Calendar.MINUTE));

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteModel noteModel = new NoteModel(title.getText().toString(), details.getText().toString(), todayDate, currentTime);
                long newNoteId = AddNote(noteModel);

                if (newNoteId != -1) {
                    Toast.makeText(AddNoteActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("title", noteModel.getNoteTitle());
                    intent.putExtra("details", noteModel.getNoteDetails());
                    intent.putExtra("date", noteModel.getNoteDate());
                    intent.putExtra("time", noteModel.getNoteTime());
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Error adding note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String pad(int i) {
        return i < 10 ? "0" + i : String.valueOf(i);
    }


    public long AddNote(NoteModel noteModel){
        DatabaseHelper db = new DatabaseHelper(AddNoteActivity.this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TITLE, noteModel.getNoteTitle());
        contentValues.put(DatabaseHelper.COLUMN_DETAILS, noteModel.getNoteDetails());
        contentValues.put(DatabaseHelper.COLUMN_DATE, noteModel.getNoteDate());
        contentValues.put(DatabaseHelper.COLUMN_TIME, noteModel.getNoteTime());
        contentValues.put(DatabaseHelper.COLUMN_USERNAME, username);

        long ID = sqLiteDatabase.insert(DatabaseHelper.DB_TABLE, null, contentValues);
        Log.d("Inserted","id --> " + ID);

        sqLiteDatabase.close();
        return ID;
    }

}
