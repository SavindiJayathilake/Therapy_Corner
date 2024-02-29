package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Arrays;


public class TheraChatActivity extends AppCompatActivity {

        private RecyclerView recyclerView;

        EditText messageInput;
        ImageButton sendMessageBtn;

        ChatRecyclerAdapterTheras adapter;

        String chatroomId;
        ImageButton backBtn;
        TextView theraName;
        private String patientUsername;
        private String therapistUsername;

        private String therapistFullname;
        ChatroomModel chatroomModel;
        private String patientFirstName;
        private String patientLastName;
        private String patientEmail;
        private String patientsUsername;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_thera_chat);

            messageInput = findViewById(R.id.chat_message_input);
            sendMessageBtn = findViewById(R.id.message_send_btn);
            backBtn = findViewById(R.id.back_btn);
            theraName = findViewById(R.id.thera_name);
            recyclerView = findViewById(R.id.chat_recycler_view);

            SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
            patientUsername = preferences.getString("username", null);

            Intent chatIntent = getIntent();
            TheraDataClass therapistData = (TheraDataClass) chatIntent.getSerializableExtra("therapistData");
            therapistUsername = chatIntent.getStringExtra("therapistUsername");



            if (therapistData != null) {
                String fullName = "Dr. " + therapistData.getTheradataFirstName() + " " + therapistData.getTheradataLastName();
                theraName.setText(fullName);

            }

            chatroomId = FirebaseUtil.getChatroomId(patientUsername,therapistUsername);




            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Patient User Details");
            usersRef.orderByChild("patientusername").equalTo(patientUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String firstName = snapshot.child("dataFirstName").getValue(String.class);
                        String lastName = snapshot.child("dataLastName").getValue(String.class);
                        String email = snapshot.child("dataEmail").getValue(String.class);
                        String patientUsername = snapshot.child("patientusername").getValue(String.class);

                        patientFirstName = firstName;
                        patientLastName = lastName;
                        patientEmail = email;
                        patientsUsername = patientUsername;


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TheraChatActivity.this, "Failed to retrieve patient details", Toast.LENGTH_SHORT).show();
                }
            });

            backBtn.setOnClickListener((v)->{
                onBackPressed();
            });

            sendMessageBtn.setOnClickListener((v -> {
                String message = messageInput.getText().toString().trim();
                if(message.isEmpty())
                    return;
                sendMessageToUser(message);
//                MessageSender messageSender = new MessageSender();
//                messageSender.sendMessageToUser(message, chatroomId, chatroomModel);
            }));

            getOrCreateChatroomModel();
            setupChatRecyclerView();
        }

    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapterTheras(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(patientUsername);
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,patientUsername,Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
//                            sendNotification(message);
                        }
                    }
                });
    }
    void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if (chatroomModel == null) {
                    //first time chat
                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(patientUsername, therapistUsername),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });





    }}
