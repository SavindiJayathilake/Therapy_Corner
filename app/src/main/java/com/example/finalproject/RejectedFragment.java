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

public class RejectedFragment extends Fragment {
    private static final String TAG = "RejectedFragment";
    private RecyclerView recyclerViewRejected;
    private RejectedAdapter rejectedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejected, container, false);
        recyclerViewRejected = view.findViewById(R.id.recyclerViewRejected);
        recyclerViewRejected.setLayoutManager(new LinearLayoutManager(getActivity()));
        rejectedAdapter = new RejectedAdapter(getContext());
        recyclerViewRejected.setAdapter(rejectedAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchDataForRejectedAppointments();
    }

    public void refreshData() {
        fetchDataForRejectedAppointments();
    }

    private void fetchDataForRejectedAppointments() {
        if (getActivity() == null) {
            Log.e(TAG, "Activity is null");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference appointmentsRef = db.collection("booking_appointments");

        SharedPreferences preferences = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String therapistUsername = preferences.getString("username", "");

        appointmentsRef.whereEqualTo("approved_status", "rejected")
                .whereEqualTo("therapist_username", therapistUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> rejectedAppointmentsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            rejectedAppointmentsList.add(appointment);
                        }
                        rejectedAdapter.setAppointments(rejectedAppointmentsList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}

