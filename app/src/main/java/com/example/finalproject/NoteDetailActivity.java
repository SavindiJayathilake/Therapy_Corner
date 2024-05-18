package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteDetailActivity extends AppCompatActivity {

    TextView showTitle, showDetails;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolerbaradddetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Entry");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showTitle = findViewById(R.id.showTitle);
        showDetails = findViewById(R.id.showDetails);

        DatabaseHelper db = new DatabaseHelper(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);

        NoteModel noteModel = db.getNotes(id);

        showTitle.setText(noteModel.getNoteTitle());
        showDetails.setText(noteModel.getNoteDetails());
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}














































//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_note_details, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_delete) {
//            deleteNote();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteNote() {
//        DatabaseHelper db = new DatabaseHelper(this);
//        db.deleteNote(id);
//        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//}






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_note_details, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            case R.id.action_share:
//                shareNoteDetails();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void shareNoteDetails() {
//        String title = showTitle.getText().toString();
//        String details = showDetails.getText().toString();
//
//        String shareBody = "Title: " + title + "\nDetails: " + details;
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Note Details");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//        startActivity(Intent.createChooser(sharingIntent, "Share via"));
//    }