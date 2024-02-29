package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PendingFragment extends Fragment implements PendingAdapter.OnButtonClickListener {
    private static final String TAG = "PendingFragment";
    private RecyclerView recyclerViewPending;
    private PendingAdapter pendingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        recyclerViewPending = view.findViewById(R.id.recyclerViewPending);
        recyclerViewPending.setLayoutManager(new LinearLayoutManager(getActivity()));
        pendingAdapter = new PendingAdapter(getContext(), new ArrayList<>(), this);
        recyclerViewPending.setAdapter(pendingAdapter);
        fetchDataForPendingAppointments();
        return view;


    }

    public void refreshData() {
        fetchDataForPendingAppointments();
    }

    private void fetchDataForPendingAppointments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference appointmentsRef = db.collection("booking_appointments");

        SharedPreferences preferences = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String therapistUsername = preferences.getString("username", "");

        appointmentsRef.whereEqualTo("approved_status", "pending")
                .whereEqualTo("therapist_username", therapistUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> pendingAppointmentsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            pendingAppointmentsList.add(appointment);
                        }
                        pendingAdapter.setAppointments(pendingAppointmentsList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void onApproveButtonClick(Appointment appointment) {
        updateStatusInFirestore(appointment, "approved");
    }

    @Override
    public void onRejectButtonClick(Appointment appointment) {
        updateStatusInFirestore(appointment, "rejected");
    }

    private void updateStatusInFirestore(Appointment appointment, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference appointmentRef = db.collection("booking_appointments").document(appointment.getBooking_id());

        appointmentRef.update("approved_status", status)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");

                    fetchDataForPendingAppointments();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    }
