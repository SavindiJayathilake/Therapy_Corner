package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class PatientChatLandingPage extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String currentUsername;

    RecentChatRecyclerAdapterTherapistInPatientsPage adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_chat_landing_page);
        recyclerView = findViewById(R.id.addRecentCoversationUsers);
        setupRecyclerView();

        Toolbar toolbar = findViewById(R.id.toolchattherabar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat Hub");


        FloatingActionButton fab = findViewById(R.id.fabithera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientChatLandingPage.this, AddTherapistToChat.class);
                startActivity(i);
            }
        });


    }

    void setupRecyclerView() {
        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        currentUsername = preferences.getString("username", null);

        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userNames", currentUsername)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);


        Toast.makeText(this, "Chat Loading", Toast.LENGTH_SHORT).show();

        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult() != null && !task.getResult().isEmpty()) {

                    Toast.makeText(this, "Chats loaded.", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "No Chats message.", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        });

        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query, ChatroomModel.class).build();

        adapter = new RecentChatRecyclerAdapterTherapistInPatientsPage(options, this);
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
