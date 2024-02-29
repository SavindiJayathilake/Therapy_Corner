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


public class PatientChatActivity extends AppCompatActivity {

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


        Intent intent = getIntent();
        if (intent != null) {

            chatroomId = intent.getStringExtra("chatroomId");
            patientUsername = intent.getStringExtra("patientUsername");
            therapistUsername = intent.getStringExtra("therapistUsername");
            String patientFirstName = intent.getStringExtra("patientFirstName");
            String patientLastName = intent.getStringExtra("patientLastName");

            theraName.setText(patientFirstName + " " + patientLastName);

            backBtn.setOnClickListener((v)->{
                onBackPressed();
            });

            sendMessageBtn.setOnClickListener((v -> {
                String message = messageInput.getText().toString().trim();
                if (message.isEmpty())
                    return;
                sendMessageToUser(message);

            }));

            getOrCreateChatroomModel();
            setupChatRecyclerView();
        }

    }
    void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapterTheras(options, getApplicationContext());
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

    void sendMessageToUser(String message) {

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(therapistUsername);
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, therapistUsername, Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");

                        }
                    }
                });
    }

    void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if (chatroomModel == null) {

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
    }
}
