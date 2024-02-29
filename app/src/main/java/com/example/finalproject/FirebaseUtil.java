package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;


public class FirebaseUtil {

    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String currentUsername() {
        SharedPreferences preferences = mContext.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    public static boolean isLoggedIn() {
        return currentUsername() != null;
    }

    public static DatabaseReference allUserCollectionReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("users");
    }

    public static DatabaseReference currentUserDetails() {
        String currentUsername = currentUsername();
        if (currentUsername != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            return database.getReference("users").child(currentUsername);
        } else {
            return null;
        }
    }
    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }


    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String username1,String username2){
        if(username1.hashCode()<username2.hashCode()){
            return username1+"_"+username2;
        }else{
            return username2+"_"+username1;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DatabaseReference getOtherUserFromChatroom(List<String> usernames) {
        String currentUserUsername = FirebaseUtil.currentUsername();
        String otherUsername = (usernames.get(0).equals(currentUserUsername)) ? usernames.get(1) : usernames.get(0);
        return allUserCollectionReference().child(otherUsername);
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }


}
