//package com.example.finalproject;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.finalproject.FirebaseUtil;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//
//public class MessageSender extends AppCompatActivity {
//
//    private ChatroomModel chatroomModel;
//    private String currentUsername;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
//        currentUsername = preferences.getString("username", null);
//    }
//
//    public void sendMessageToUser(String message, String chatroomId, ChatroomModel chatroomModel) {
//        this.chatroomModel = chatroomModel;
//
//        chatroomModel.setLastMessageTimestamp(Timestamp.now());
//        chatroomModel.setLastMessageSenderId(currentUsername);
//        chatroomModel.setLastMessage(message);
//        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
//
//        ChatMessageModel chatMessageModel = new ChatMessageModel(message, currentUsername, Timestamp.now());
//        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            messageInput.setText("");
//                            sendNotification(message);
//                        }
//                    }
//                });
//    }
//}
