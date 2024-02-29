package com.example.finalproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etFirstName, etEmail, etUsername, etPassword;
    private RadioButton radioButtonTherapist, radioButtonPatient;

    FirebaseDatabase userdatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        btnRegister = findViewById(R.id.btnRegister);
        etFirstName = findViewById(R.id.etFirstName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        radioButtonTherapist = findViewById(R.id.radioButtonTherapist);
        radioButtonPatient = findViewById(R.id.radioButtonPatient);

        radioButtonTherapist.setChecked(true);

        TextView loginRedirectText = findViewById(R.id.loginRedirectText);
        String text = "Already have an account?  Login";
        SpannableString ss = new SpannableString(text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ss.setSpan(boldSpan, 26, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(underlineSpan, 26, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginRedirectText.setText(ss);

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userdatabase = FirebaseDatabase.getInstance();
                databaseReference = userdatabase.getReference("users");
                final String firstName = etFirstName.getText().toString();
                final String email = etEmail.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String role = radioButtonTherapist.isChecked() ? "Therapist" : "Patient";
                final HelperClass helperClass = new HelperClass(firstName, email, username, password, role);

                databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Toast.makeText(RegisterActivity.this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show();
                        } else {

                            databaseReference.child(username).setValue(helperClass);
                            Toast.makeText(RegisterActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("ROLE", role); // Add this line to pass the role
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("RegisterActivity", "Error checking username existence", databaseError.toException());
                    }
                });
            }
        });

    }
}
