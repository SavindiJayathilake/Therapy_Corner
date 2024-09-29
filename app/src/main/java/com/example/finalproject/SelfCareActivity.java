package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfCareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_care);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Self-Care");

        TextView quoteTextView = findViewById(R.id.quoteTextView);
        quoteTextView.setText("I'm doing this for me.");

        ImageView eJournalButton = findViewById(R.id.eJournalButton);
        eJournalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelfCareActivity.this, EJournalActivity.class));
            }
        });

        ImageView moodTrackerButton = findViewById(R.id.moodTrackerButton);
        moodTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelfCareActivity.this, MoodTrackerActivity.class));
            }
        });

        ImageView relaxingSoundsButton = findViewById(R.id.relaxingSoundsButton);
        relaxingSoundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelfCareActivity.this, RelaxingSounds.class));
            }
        });

        ImageView articlesButton = findViewById(R.id.articlesButton);
        articlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelfCareActivity.this, DailyBoostActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
