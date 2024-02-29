package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PatientHomepageActivity extends AppCompatActivity implements View.OnClickListener {


     String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_homepage);

        ImageView btnSearchTherapist = findViewById(R.id.btnSearchTherapist);
        ImageView btnBookTherapist = findViewById(R.id.btnBookTherapist);
        ImageView btnChatWithTherapist = findViewById(R.id.btnChatWithTherapist);
        ImageView btnSelfCare = findViewById(R.id.btnSelfCare);
        ImageView btnUserProfile = findViewById(R.id.btnUserProfile);


        btnSearchTherapist.setOnClickListener(this);
        btnBookTherapist.setOnClickListener(this);
        btnChatWithTherapist.setOnClickListener(this);
        btnSelfCare.setOnClickListener(this);
        btnUserProfile.setOnClickListener(this);


        username = getIntent().getStringExtra("username");

        TextView textHelloPatient = findViewById(R.id.textHelloPatient);
        textHelloPatient.setText("Hello " + username);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchTherapist:
                startActivity(new Intent(this, MatchTherapist.class));
                break;
            case R.id.btnBookTherapist:
                startActivity(new Intent(this, SearchTherapist.class));
                break;
            case R.id.btnChatWithTherapist:
                startActivity(new Intent(this, TherapistChatLandingPage.class));
                break;
            case R.id.btnSelfCare:
                startActivity(new Intent(this, SelfCareActivity.class));
                break;
            case R.id.btnUserProfile:
                Intent userProfileIntent = new Intent(this, UserPatientProfileActivity.class);
                userProfileIntent.putExtra("username", username);
                startActivity(userProfileIntent);
                break;
        }
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
