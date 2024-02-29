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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ApprovedFragment extends Fragment {
    private static final String TAG = "ApprovedFragment";
    private RecyclerView recyclerViewApproved;
    private ApprovedAdapter approvedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approved, container, false);
        recyclerViewApproved = view.findViewById(R.id.recyclerViewApproved);
        recyclerViewApproved.setLayoutManager(new LinearLayoutManager(getActivity()));
        approvedAdapter = new ApprovedAdapter(getContext());
        recyclerViewApproved.setAdapter(approvedAdapter);
        fetchDataForApprovedAppointments();
        return view;
    }

    public void refreshData() {
        fetchDataForApprovedAppointments();
    }

    private void fetchDataForApprovedAppointments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference appointmentsRef = db.collection("booking_appointments");

        SharedPreferences preferences = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String therapistUsername = preferences.getString("username", "");

        appointmentsRef.whereEqualTo("approved_status", "approved")
                .whereEqualTo("therapist_username", therapistUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> approvedAppointmentsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            approvedAppointmentsList.add(appointment);
                        }
                        approvedAdapter.setAppointments(approvedAppointmentsList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
