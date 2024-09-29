package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MatchTherapist extends AppCompatActivity {

    private DatabaseReference therapistRef;
    private Spinner locationSpinner;
    private Spinner platformSpinner;
//    private Spinner communicationMediumSpinner;

    private Spinner languagesSpinner;
    private Spinner therapyServicesSpinner;

    private Spinner genderSpinner;
    private TextView resultTextView;

    private ImageView profileImageView;
    private TextView therapistNameTextView;
    private TextView servicesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_therapist);

        Toolbar toolbar = findViewById(R.id.toolkbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match Therapist");

        therapistRef = FirebaseDatabase.getInstance().getReference().child("Therapist User Details");
        locationSpinner = findViewById(R.id.location_spinner);
        platformSpinner = findViewById(R.id.platform_spinner);
//        communicationMediumSpinner = findViewById(R.id.communication_medium_spinner);
//        availableDaysSpinner = findViewById(R.id.available_days_spinner);
        languagesSpinner = findViewById(R.id.languages_spinner);
        therapyServicesSpinner = findViewById(R.id.therapy_services_spinner);
//        ageGroupSpinner = findViewById(R.id.age_group_spinner);
        genderSpinner = findViewById(R.id.gender_spinner);
//        resultTextView = findViewById(R.id.result_text_view);
        profileImageView = findViewById(R.id.profile_image);
        therapistNameTextView = findViewById(R.id.therapist_name);
        servicesTextView = findViewById(R.id.services_text);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> calculateBestMatch());
    }

    private void calculateBestMatch() {
        String preferredLocation = locationSpinner.getSelectedItem().toString();
        String preferredPlatform = platformSpinner.getSelectedItem().toString();
//        String preferredCommunicationMedium = communicationMediumSpinner.getSelectedItem().toString();
//        String preferredAvailableDays = availableDaysSpinner.getSelectedItem().toString();
        String preferredLanguages = languagesSpinner.getSelectedItem().toString();
        String preferredTherapyServices = therapyServicesSpinner.getSelectedItem().toString();
//        String preferredAgeGroup = ageGroupSpinner.getSelectedItem().toString();
        String preferredGender = genderSpinner.getSelectedItem().toString();

        therapistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double maxMatchScore = -1;
                String bestMatchTherapist = "";

                for (DataSnapshot therapistSnapshot : snapshot.getChildren()) {
                    double matchScore = calculateMatchScore(
                            preferredLocation, preferredPlatform,
                             preferredLanguages, preferredTherapyServices,
                             preferredGender, therapistSnapshot);

                    if (matchScore > maxMatchScore) {
                        maxMatchScore = matchScore;
                        bestMatchTherapist = therapistSnapshot.getKey();
                    }
                }

                if (!bestMatchTherapist.isEmpty()) {
                    displayBestMatchTherapist(bestMatchTherapist);

                } else {
                    resultTextView.setText("No match found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private double calculateMatchScore(String userLocation, String userPlatform,
                                       String userLanguages, String userTherapyServices, String userGender, DataSnapshot therapistSnapshot) {
        double locationScore = userLocation.equals(therapistSnapshot.child("theradataArea").getValue(String.class)) ? 1 : 0;
        double platformScore = userPlatform.equals(therapistSnapshot.child("theradataServicesPlatforms").getValue(String.class)) ? 1 : 0;
//        double communicationMediumScore = userCommunicationMedium.equals(therapistSnapshot.child("theradataCommunicationMediums").getValue(String.class)) ? 1 : 0;

        double languagesScore = userLanguages.equals(therapistSnapshot.child("theradataOfferedLanguages").getValue(String.class)) ? 1 : 0;
        double therapyServicesScore = userTherapyServices.equals(therapistSnapshot.child("theradataTherapyServices").getValue(String.class)) ? 1 : 0;
        double genderScore = userGender.equals(therapistSnapshot.child("theradataGender").getValue(String.class)) ? 1 : 0;

        return locationScore + platformScore  + languagesScore + therapyServicesScore  + genderScore;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void displayBestMatchTherapist(String therapistId) {
        DatabaseReference therapistDetailsRef = therapistRef.child(therapistId);
        CardView resultCard = findViewById(R.id.result_card);

        therapistDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("theradataFirstName").getValue(String.class);
                String lastName = snapshot.child("theradataLastName").getValue(String.class);
                String services = snapshot.child("theradataTherapyServices").getValue(String.class);
                String profileImageUrl = snapshot.child("theradataImage").getValue(String.class);

                if (firstName != null && lastName != null) {
                    String fullName = "Dr. " + firstName + " " + lastName;
                    therapistNameTextView.setText(fullName);
                } else {
                    therapistNameTextView.setText("Name information not available");
                }

                if (services != null) {
                    servicesTextView.setText(services);
                } else {
                    servicesTextView.setText("Services information not available");
                }


                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {

                    resultCard.setVisibility(View.INVISIBLE);
                    profileImageView.setVisibility(View.INVISIBLE);
                    therapistNameTextView.setVisibility(View.INVISIBLE);
                    servicesTextView.setVisibility(View.INVISIBLE);

                    Picasso.get().load(profileImageUrl).into(profileImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                            resultCard.setVisibility(View.VISIBLE);
                            profileImageView.setVisibility(View.VISIBLE);
                            therapistNameTextView.setVisibility(View.VISIBLE);
                            servicesTextView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                } else {

                    profileImageView.setImageResource(R.drawable.default_profile_image);


                    resultCard.setVisibility(View.VISIBLE);
                    profileImageView.setVisibility(View.VISIBLE);
                    therapistNameTextView.setVisibility(View.VISIBLE);
                    servicesTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
