package com.example.finalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class RecentChatRecyclerAdapterPatient  extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapterPatient.ChatroomModelViewHolder> {

    Context context;

    private String patientFirstName;
    private String patientLastName;

    public RecentChatRecyclerAdapterPatient(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentChatRecyclerAdapterPatient.ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {

        SharedPreferences preferences = holder.itemView.getContext().getSharedPreferences("user_details", MODE_PRIVATE);
        String currentUsername = preferences.getString("username", null);


        //Assuming model.getuserNames() returns a List with patient's username as the first element
        // and therapist's username as the second element
        List<String> userNames = model.getuserNames();
        String patientUsername = userNames.get(0);
        String therapistUsername = userNames.get(1);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Patient User Details");
        usersRef.orderByChild("patientusername").equalTo(patientUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    patientFirstName = snapshot.child("dataFirstName").getValue(String.class);
                    patientLastName = snapshot.child("dataLastName").getValue(String.class);



                    if(currentUsername.equals(therapistUsername)) {
                        holder.usernameText.setText(patientFirstName + " " + patientLastName);
                    } else {
                        holder.usernameText.setText(patientUsername);
                    }

                    boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(currentUsername);
                    if (lastMessageSentByMe) {
                        holder.lastMessageText.setText("You: " + model.getLastMessage());
                    } else {
                        holder.lastMessageText.setText(model.getLastMessage());
                    }
                    holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors that occurred during the read operation
            }
        });


        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, PatientChatActivity.class);
            intent.putExtra("chatroomId", model.getChatroomId());
            intent.putExtra("patientUsername", patientUsername);
            intent.putExtra("therapistUsername", therapistUsername);
            intent.putExtra("patientFirstName", patientFirstName);
            intent.putExtra("patientLastName", patientLastName);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        });

    }

    @NonNull
    @Override
    public RecentChatRecyclerAdapterPatient.ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new RecentChatRecyclerAdapterPatient.ChatroomModelViewHolder(view);
    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{


        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;


        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.therapist_name);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
        }
    }
}
