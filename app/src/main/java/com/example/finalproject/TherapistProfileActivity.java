package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class TherapistProfileActivity extends AppCompatActivity {

    private ImageView profileImage;

    String imageURL;
    Uri uri;

    String username;

    private Spinner spinnerGender;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private Button btnSave;
    private EditText etOfficeAddress;
    private Spinner spinnerArea;
//    private Spinner spinnerCommunicationMediums;
    private Spinner spinnerServicesPlatforms;
    private Spinner spinnerOfferedLanguages;
    private Spinner spinnerTherapyServices;
    private Button btnCancel;

    private EditText et_month_pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_therapist_profile);

        profileImage = findViewById(R.id.profile_image);
        spinnerGender = findViewById(R.id.spinner_gender);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        etOfficeAddress = findViewById(R.id.et_office_address);
        spinnerArea = findViewById(R.id.spinner_area);
        spinnerTherapyServices = findViewById(R.id.spinner_therapy_services);
        spinnerOfferedLanguages = findViewById(R.id.spinner_offered_languages);
        spinnerServicesPlatforms = findViewById(R.id.spinner_services_platforms);
//        spinnerCommunicationMediums = findViewById(R.id.spinner_communication_mediums);
        et_month_pay = findViewById(R.id.et_month_pay);

        Toolbar toolbar = findViewById(R.id.toolynbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Profile");

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        username = preferences.getString("username", null);

        if (username != null) {
            FirebaseDatabase.getInstance().getReference("Therapist User Details")
                    .child(username)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                TheraDataClass theraDataClass = snapshot.getValue(TheraDataClass.class);
                                if (theraDataClass != null) {
                                    etFirstName.setText(theraDataClass.getTheradataFirstName());
                                    etLastName.setText(theraDataClass.getTheradataLastName());
                                    etEmail.setText(theraDataClass.getTheradataEmail());
                                    etPhoneNumber.setText(theraDataClass.getTheradataPhone());
                                    String imageURL = theraDataClass.getTheradataImage();
                                    Picasso.get().load(imageURL).into(profileImage);
                                    etOfficeAddress.setText(theraDataClass.getTheradataOfficeAddress());
                                    theraDataClass.setTherapistusername(username);


    spinnerGender.setSelection(((ArrayAdapter<String>) spinnerGender.getAdapter()).getPosition(theraDataClass.getTheradataGender()));
    spinnerArea.setSelection(((ArrayAdapter<String>) spinnerArea.getAdapter()).getPosition(theraDataClass.getTheradataArea()));
    spinnerTherapyServices.setSelection(((ArrayAdapter<String>) spinnerTherapyServices.getAdapter()).getPosition(theraDataClass.getTheradataTherapyServices()));
    //    spinnerCommunicationMediums.setSelection(((ArrayAdapter<String>) spinnerCommunicationMediums.getAdapter()).getPosition(theraDataClass.getTheradataCommunicationMediums()));
    spinnerServicesPlatforms.setSelection(((ArrayAdapter<String>) spinnerServicesPlatforms.getAdapter()).getPosition(theraDataClass.getTheradataServicesPlatforms()));
    spinnerOfferedLanguages.setSelection(((ArrayAdapter<String>) spinnerOfferedLanguages.getAdapter()).getPosition(theraDataClass.getTheradataOfferedLanguages()));

                                    et_month_pay.setText(String.valueOf(theraDataClass.getTheradataMonthlyPay()));

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(TherapistProfileActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            profileImage.setImageURI(uri);
                        } else {
                            Toast.makeText(TherapistProfileActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    saveData();
                    Toast.makeText(TherapistProfileActivity.this, "Saving!", Toast.LENGTH_SHORT).show();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileImage.setImageResource(R.drawable.default_profile_image);

                etFirstName.setText("");
                etLastName.setText("");
                etEmail.setText("");
                etPhoneNumber.setText("");
                etOfficeAddress.setText("");
                spinnerArea.setSelection(0);
                spinnerTherapyServices.setSelection(0);
                spinnerOfferedLanguages.setSelection(0);
                spinnerServicesPlatforms.setSelection(0);
//                spinnerCommunicationMediums.setSelection(0);


                Toast.makeText(TherapistProfileActivity.this, "Changes cancelled", Toast.LENGTH_SHORT).show();
            }
        });



    }



    private boolean validateFields() {
        return uri != null && !etFirstName.getText().toString().isEmpty() &&
                !etLastName.getText().toString().isEmpty() &&
                !etEmail.getText().toString().isEmpty() &&
                !etPhoneNumber.getText().toString().isEmpty() &&
                !etOfficeAddress.getText().toString().isEmpty() &&
                !et_month_pay.getText().toString().isEmpty() &&
                spinnerArea.getSelectedItemPosition() > 0 &&

                spinnerServicesPlatforms.getSelectedItemPosition() > 0 &&
                spinnerTherapyServices.getSelectedItemPosition() > 0 &&
                spinnerOfferedLanguages.getSelectedItemPosition() > 0 &&

//                spinnerCommunicationMediums.getSelectedItemPosition() > 0 &&
                spinnerGender.getSelectedItemPosition() > 0;
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveData() {

        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Therapist User Images")
                    .child(username)
                    .child(uri.getLastPathSegment());

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TherapistProfileActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(TherapistProfileActivity.this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }



    public void uploadData() {

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String officeAddress = etOfficeAddress.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String area = spinnerArea.getSelectedItem().toString();
        double monthlyPay = Double.parseDouble(et_month_pay.getText().toString());

        String servicesPlatforms = spinnerServicesPlatforms.getSelectedItem().toString();
        String therapyServices = spinnerTherapyServices.getSelectedItem().toString();
        String offeredLanguages = spinnerOfferedLanguages.getSelectedItem().toString();
//        String availableDays = spinnerAvailableDays.getSelectedItem().toString();
//        String communicationMediums = spinnerCommunicationMediums.getSelectedItem().toString();

        TheraDataClass theraDataClass = new TheraDataClass(
                firstName, lastName, email, phoneNumber, imageURL, gender,
                officeAddress, area, therapyServices, offeredLanguages,  servicesPlatforms , monthlyPay );

        theraDataClass.setTherapistusername(username);

        if (username != null) {
            FirebaseDatabase.getInstance().getReference("Therapist User Details")
                    .child(username)
                    .setValue(theraDataClass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TherapistProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TherapistProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(TherapistProfileActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
        }
    }




}



