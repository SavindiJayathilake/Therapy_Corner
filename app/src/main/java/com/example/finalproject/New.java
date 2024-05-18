//package com.example.finalproject;
//
//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatImageView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.lang.reflect.MalformedParameterizedTypeException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class AddTherapistToChat extends AppCompatActivity {
//
//    private EditText searchEditText;
//
//    private DatabaseReference databaseReference;
//
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_therapist_chat_users);
//
//
//        searchEditText = findViewById(R.id.search_edit_text_chat);
//        databaseReference = FirebaseDatabase.getInstance().getReference("Therapist User Details");
//        db = FirebaseFirestore.getInstance();
//
//
//        Button searchButton = findViewById(R.id.search_button);
//
//
//
//        Intent intent = getIntent();
//        TheraDataClass therapistData = (TheraDataClass) intent.getSerializableExtra("therapistData");
//
//        AppCompatImageView imageBack = findViewById(R.id.imageBack);
//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                performSearch();
//            }
//        });
//    }
//
//
//    private void performSearch() {
//        Log.d("SearchTherapist", "Performing search...");
//        String searchText = searchEditText.getText().toString().toLowerCase();
//
//
//        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
//        String patientUsername = preferences.getString("username", null);
//
//        Query query = databaseReference.orderByChild("theradataFirstName");
//
//
//        CollectionReference appointmentsRef = db.collection("booking_appointments");
//
//
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Map<String, TheraDataClass> therapistsMap = new HashMap<>();
//
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        TheraDataClass therapist = snapshot.getValue(TheraDataClass.class);
//                        if (therapist != null) {
//                            String firstName = therapist.getTheradataFirstName();
//                            if (firstName != null && firstName.toLowerCase().contains(searchText)) {
//                                therapistsMap.put(therapist.getTherapistusername(), therapist);
//                            }
//
//                        }
//                    }
//
//                    List<Appointment> appointments = new ArrayList<>();
//                    appointmentsRef.whereEqualTo("approved_status", "pending")
//                            .whereEqualTo("patient_username", patientUsername)
//                            .get()
//                            .addOnCompleteListener(task -> {
//
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Appointment appointment = document.toObject(Appointment.class);
//                                        appointments.add(appointment);
//                                    }
//                                    // filter therapist based on appointments
//                                    List<TheraDataClass> finalTherapists = new ArrayList<>();
//                                    for (Appointment appointment : appointments) {
//                                        TheraDataClass therapist = therapistsMap.get(appointment.getTherapist_username());
//                                        if (therapist != null) {
//                                            finalTherapists.add(therapist);
//                                        }
//                                    }
//
//                                    RecyclerView recyclerView = findViewById(R.id.chat_thera_user_recycler);
//                                    recyclerView.setLayoutManager(new LinearLayoutManager(AddTherapistToChat.this));
//                                    recyclerView.setAdapter(new TherapistCardChatUserAdapter(finalTherapists));
//
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//            }
//        });
//    }
//
//}