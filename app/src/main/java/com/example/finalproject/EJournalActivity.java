package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EJournalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<NoteModel> noteModelList;

    private static final int REQUEST_CODE_ADD_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejournal);

        recyclerView = findViewById(R.id.addRecyclerView);

        Toolbar toolbar = findViewById(R.id.toolsbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gratitude Journal");

        noteModelList = getNote();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this, noteModelList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EJournalActivity.this, AddNoteActivity.class);
                startActivityForResult(i, REQUEST_CODE_ADD_NOTE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("title");
                String details = data.getStringExtra("details");
                String date = data.getStringExtra("date");
                String time = data.getStringExtra("time");

                NoteModel newNote = new NoteModel(title, details, date, time);
                noteModelList.add(0, newNote);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public List<NoteModel> getNote(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<NoteModel> allNote = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        String queryStatement = "SELECT * FROM " + DatabaseHelper.DB_TABLE +
                " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(queryStatement, new String[]{username});

        if (cursor.moveToFirst()){
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setId(cursor.getInt(0));
                noteModel.setNoteTitle(cursor.getString(1));
                noteModel.setNoteDetails(cursor.getString(2));
                noteModel.setNoteDate(cursor.getString(3));
                noteModel.setNoteTime(cursor.getString(4));

                allNote.add(noteModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return allNote;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}






