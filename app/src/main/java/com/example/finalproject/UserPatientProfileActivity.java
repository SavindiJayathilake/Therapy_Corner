package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.text.DateFormat;
import java.util.Calendar;

public class UserPatientProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPhoneNumber;
    Button btnSave;
    Button btnCancel;
    String imageURL;
    Uri uri;
    String username;

    private String patientusername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_patient_profile);

        profileImage = findViewById(R.id.profile_image);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        Toolbar toolbar = findViewById(R.id.toolybar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Profile");

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        username = preferences.getString("username", null);

        if (username != null) {
            FirebaseDatabase.getInstance().getReference("Patient User Details")
                    .child(username)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DataClass dataClass = snapshot.getValue(DataClass.class);
                                if (dataClass != null) {
                                    etFirstName.setText(dataClass.getDataFirstName());
                                    etLastName.setText(dataClass.getDataLastName());
                                    etEmail.setText(dataClass.getDataEmail());
                                    etPhoneNumber.setText(dataClass.getDataPhone());
                                    String imageURL = dataClass.getDataImage();
                                    Picasso.get().load(imageURL).into(profileImage);
                                    }
                                }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UserPatientProfileActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            profileImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UserPatientProfileActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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
                if (validateFields()) {
                    saveData();
                } else {
                    Toast.makeText(UserPatientProfileActivity.this, "Please fill in all mandatory fields.", Toast.LENGTH_SHORT).show();
                }
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


                Toast.makeText(UserPatientProfileActivity.this, "Changes cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateFields() {
        return uri != null && !etFirstName.getText().toString().isEmpty() &&
                !etLastName.getText().toString().isEmpty() &&
                !etEmail.getText().toString().isEmpty() &&
                !etPhoneNumber.getText().toString().isEmpty();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveData(){
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Patient User Images")
                    .child(username)
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UserPatientProfileActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
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
            Toast.makeText(UserPatientProfileActivity.this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadData(){
        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhoneNumber.getText().toString();
        DataClass dataClass = new DataClass(firstname, lastname, email, phone, imageURL);

        dataClass.setPatientusername(username);

        if (username != null) {
            FirebaseDatabase.getInstance().getReference("Patient User Details")
                    .child(username)
                    .setValue(dataClass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                savePatientDetailsToSharedPreferences(username);
                                Toast.makeText(UserPatientProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserPatientProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(UserPatientProfileActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePatientDetailsToSharedPreferences(String username) {
        FirebaseDatabase.getInstance().getReference("Patient User Details")
                .child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataClass dataClass = snapshot.getValue(DataClass.class);
                            if (dataClass != null) {
                                SharedPreferences preferencesp = getSharedPreferences("patient_details", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesp.edit();
                                editor.putString("patientFirstName", dataClass.getDataFirstName());
                                editor.putString("patientLastName", dataClass.getDataLastName());
                                editor.putString("patientEmail", dataClass.getDataEmail());
                                editor.apply();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserPatientProfileActivity.this, "Failed to retrieve patient data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
