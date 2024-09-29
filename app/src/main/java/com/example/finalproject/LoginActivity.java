package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        TextView signupRedirectText = findViewById(R.id.signupRedirectText);
        String text = "Not yet registered?  Sign Up";

        SpannableString ss = new SpannableString(text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        UnderlineSpan underlineSpan = new UnderlineSpan();

        ss.setSpan(boldSpan, 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(underlineSpan, 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupRedirectText.setText(ss);

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    checkCredentials(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkCredentials(String username, String password) {
        databaseReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass helperClass = dataSnapshot.getValue(HelperClass.class);
                    if (helperClass != null && helperClass.getPassword().equals(password)) {
                        String role = helperClass.getRole();
                        saveUsername(username);
                        redirectToHomePage(role, username);
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found. Please register.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUsername(String username) {
        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void redirectToHomePage(String role, String username) {
        Intent intent;
        if (role.equals("Therapist")) {
            intent = new Intent(LoginActivity.this, TherapistHomeActivity.class);
            intent.putExtra("username", username);
        } else if (role.equals("Patient")) {
            intent = new Intent(LoginActivity.this, PatientHomepageActivity.class);
            intent.putExtra("username", username);
        } else {
            Toast.makeText(LoginActivity.this, "Unknown role. Please contact support.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }

}
