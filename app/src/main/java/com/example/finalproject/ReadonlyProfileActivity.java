package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ReadonlyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readonly_profile);

        Toolbar toolbar = findViewById(R.id.toolerbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        TheraDataClass therapistData = (TheraDataClass) intent.getSerializableExtra("therapistData");

        if (therapistData != null) {

            ImageView profileImageView = findViewById(R.id.profile_image);
            TextView therapistNameTextView = findViewById(R.id.therapist_name);
            TextView therapyServiceTextView = findViewById(R.id.therapy_service);
            TextView monthlypaymentTextView = findViewById(R.id.payment);
            TextView emailTextView = findViewById(R.id.email);
            TextView phoneNumberTextView = findViewById(R.id.phone_number);
            TextView addressTextView = findViewById(R.id.address);
            TextView languages = findViewById(R.id.languages);
            TextView communicationmediums = findViewById(R.id.communication_mediums);
//            Button chat = findViewById(R.id.selecttochat);
            Button book = findViewById(R.id.selecttobook);


            String fullName = "Dr. " + therapistData.getTheradataFirstName() + " " + therapistData.getTheradataLastName();
            therapistNameTextView.setText(fullName);

            therapyServiceTextView.setText(therapistData.getTheradataTherapyServices());

            double increasedPayment = therapistData.getTheradataMonthlyPay() * 1.20;
            String formattedIncreasedPayment = String.format("%.2f", increasedPayment);


            SpannableString boldIncreasedPayment = new SpannableString(formattedIncreasedPayment);
            boldIncreasedPayment.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, boldIncreasedPayment.length(), 0);


            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("LKR ");
            builder.append(boldIncreasedPayment);
            builder.append(" / session");


            monthlypaymentTextView.setText(builder);


            emailTextView.setText("Email: " + therapistData.getTheradataEmail());
            phoneNumberTextView.setText("Phone Number: " + therapistData.getTheradataPhone());
            addressTextView.setText("Address: " + therapistData.getTheradataOfficeAddress());
            languages.setText("Languages: " + therapistData.getTheradataOfferedLanguages());
            communicationmediums.setText("Contact Mediums: " + therapistData.getTheradataServicesPlatforms());


            String profileImageUrl = therapistData.getTheradataImage();
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                Picasso.get().load(profileImageUrl).into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                        profileImageView.setImageResource(R.drawable.default_profile_image);
                    }
                });
            } else {

                profileImageView.setImageResource(R.drawable.default_profile_image);
            }



            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent bookingIntent = new Intent(ReadonlyProfileActivity.this, BookingTherapistActivity.class);
                    bookingIntent.putExtra("therapistName", fullName);
                    bookingIntent.putExtra("therapyService", therapistData.getTheradataTherapyServices());
                    bookingIntent.putExtra("therapistUsername", therapistData.getTherapistusername());
                    bookingIntent.putExtra("therapistEmail", therapistData.getTheradataEmail());
                    bookingIntent.putExtra("formattedPayment", builder.toString());

                    startActivity(bookingIntent);
                }
            });



//            chat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent chatIntent = new Intent(ReadonlyProfileActivity.this, ChatActivity.class);
//                    chatIntent.putExtra("therapistUsername", therapistData.getTherapistusername());
//                    chatIntent.putExtra("therapistName", fullName);
//                    chatIntent.putExtra("profileImageUrl", profileImageUrl);
//                    startActivity(chatIntent);
//                }
//
//            });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}