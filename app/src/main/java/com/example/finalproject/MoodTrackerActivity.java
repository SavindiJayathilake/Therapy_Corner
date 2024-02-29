package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MoodTrackerActivity extends AppCompatActivity {

    private MoodDataSource dataSource;
    private Spinner moodSpinner;
    private EditText reasonEditText;
    private Button saveButton;
    private TextView savedMoodTextView;
    private Button selectDateButton;
    private String selectedDate;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);

        Toolbar toolbar = findViewById(R.id.toolsbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mood Tracker");

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        username = preferences.getString("username", "");

        dataSource = new MoodDataSource(this);
        dataSource.open();

        moodSpinner = findViewById(R.id.moodSpinner);
        reasonEditText = findViewById(R.id.reasonEditText);
        saveButton = findViewById(R.id.saveButton);
        savedMoodTextView = findViewById(R.id.savedMoodTextView);
        selectDateButton = findViewById(R.id.selectDateButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.moods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    String mood = moodSpinner.getSelectedItem().toString();
                    String reason = reasonEditText.getText().toString();

                    MoodEntry moodEntry = new MoodEntry();
                    moodEntry.setDate(selectedDate);
                    moodEntry.setMood(mood);
                    moodEntry.setReason(reason);

                    dataSource.insertMood(moodEntry, username);

                    Toast.makeText(MoodTrackerActivity.this, "Result saved for " + selectedDate, Toast.LENGTH_SHORT).show();

                    savedMoodTextView.setText("My Mood: " + moodEntry.getMood() + "\n" + "\nReason: " + moodEntry.getReason());

                    moodSpinner.setSelection(0);
                    reasonEditText.setText("");
                }
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

                        MoodTrackerActivity.this.selectedDate = formattedDate;
                        savedMoodTextView.setText("");
                        reasonEditText.setText("");
                        moodSpinner.setSelection(0);

                        List<MoodEntry> moods = dataSource.getMoodsForDateAndUsername(selectedDate, username);

                        if (!moods.isEmpty()) {
                            MoodEntry moodEntry = moods.get(moods.size() - 1);
                            savedMoodTextView.setText("My Mood: " + moodEntry.getMood() + "\n" + "\nReason: " + moodEntry.getReason());
                        } else {
                            savedMoodTextView.setText("No mood saved for this date");
                        }
                    }

                },
                year, month, day);
        datePickerDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
