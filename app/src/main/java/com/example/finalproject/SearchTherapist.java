package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchTherapist extends AppCompatActivity  {
    private EditText searchEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_therapist);

        Toolbar toolbar = findViewById(R.id.tooltbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Therapist");

        searchEditText = findViewById(R.id.search_edit_text);
        databaseReference = FirebaseDatabase.getInstance().getReference("Therapist User Details");

        Button searchButton = findViewById(R.id.search_button);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }


    private void performSearch() {
        Log.d("SearchTherapist", "Performing search...");
        String searchText = searchEditText.getText().toString().toLowerCase();

        Query query = databaseReference.orderByChild("theradataFirstName");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TheraDataClass> therapists = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TheraDataClass therapist = snapshot.getValue(TheraDataClass.class);
                        if (therapist != null) {
                            String firstName = therapist.getTheradataFirstName();
                            if (firstName != null && firstName.toLowerCase().contains(searchText)) {
                                therapists.add(therapist);
                            }
                        }
                    }
                }

                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchTherapist.this));
                recyclerView.setAdapter(new TherapistCardAdapter(therapists));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
