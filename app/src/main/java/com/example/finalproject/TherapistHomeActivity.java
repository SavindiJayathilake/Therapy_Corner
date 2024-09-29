package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TherapistHomeActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_home);

        username = getIntent().getStringExtra("username");

        TextView textHelloPatient = findViewById(R.id.welcomeText);
        textHelloPatient.setText("Hello " + username);

        ImageView viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        viewAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TherapistHomeActivity.this, TherapistBookingApproval.class));
            }
        });

        ImageView chatWithPatientButton = findViewById(R.id.chatWithPatientButton);
        chatWithPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TherapistHomeActivity.this, TherapistsChatLandingPage.class));
            }
        });

        ImageView userProfileButton = findViewById(R.id.userProfileButton);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfileIntent = new Intent(TherapistHomeActivity.this, TherapistProfileActivity.class);
                userProfileIntent.putExtra("username", username);
                startActivity(userProfileIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        moveTaskToBack(true);
    }
    public void logoutUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

}

