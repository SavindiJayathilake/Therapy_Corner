package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointments extends AppCompatActivity {
    private static final String TAG = "PatientAppointments";
    private RecyclerView recyclerViewAppointments;
    private PatientAppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        Toolbar toolbar = findViewById(R.id.toolbarappoint);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Appointments");

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter = new PatientAppointmentAdapter(this, new ArrayList<>());
        recyclerViewAppointments.setAdapter(appointmentAdapter);

        fetchPatientAppointments();
    }

    private void fetchPatientAppointments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference appointmentsRef = db.collection("booking_appointments");

        SharedPreferences preferences = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String patientUsername = preferences.getString("username", "");

        appointmentsRef.whereEqualTo("patient_username", patientUsername)
                .whereEqualTo("approved_status", "approved")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> patientAppointmentsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            patientAppointmentsList.add(appointment);
                        }
                        appointmentAdapter.setAppointments(patientAppointmentsList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
