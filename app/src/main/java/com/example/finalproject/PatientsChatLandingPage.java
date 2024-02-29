package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class PatientsChatLandingPage extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String currentUsername;

    RecentChatRecyclerAdapterPatient adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_chat_landing_page);
        recyclerView = findViewById(R.id.addRecentCoversationPatients);
        setupRecyclerView();


        Toolbar toolbar = findViewById(R.id.toolchatpatabar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat Hub");


    }

        void setupRecyclerView() {
            SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
            currentUsername = preferences.getString("username", null);

            com.google.firebase.firestore.Query query = FirebaseUtil.allChatroomCollectionReference()
                    .whereArrayContains("userNames", currentUsername)
                    .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

            // Debugging: Check if the query is good
            Toast.makeText(this, "Querying Firestore...", Toast.LENGTH_SHORT).show();

            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    if(task.getResult() != null && !task.getResult().isEmpty()) {
                        // Chats are available
                        Toast.makeText(this, "Chats loaded.", Toast.LENGTH_SHORT).show();
                    } else {
                        // No chats available
                        Toast.makeText(this, "No Chats message.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Query failed
                    Toast.makeText(this, "Failed to load chats.", Toast.LENGTH_SHORT).show();
                }
            });

            FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                    .setQuery(query, ChatroomModel.class).build();

            adapter = new RecentChatRecyclerAdapterPatient(options, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }


        @Override
        public void onStart() {
            super.onStart();
            if(adapter!=null)
                adapter.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            if(adapter!=null)
                adapter.stopListening();
        }

        @Override
        public void onResume() {
            super.onResume();
            if(adapter!=null)
                adapter.notifyDataSetChanged();
        }




        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
        }
    }
