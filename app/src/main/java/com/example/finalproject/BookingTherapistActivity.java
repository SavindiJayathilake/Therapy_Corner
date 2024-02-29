package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.ArrayList;



public class BookingTherapistActivity extends AppCompatActivity {

    private TextView doctorNameTextView;
    private TextView payAmountTextView;
    private TextView therapyServiceTextView;
    private Button datePickerButton;
    private Spinner timeSlotsSpinner;
    private Button bookButton;
    private TextView selectedDateTextView;

    private String selectedDateStr;
    private Time selectedTimeSlot;

    private FirebaseFirestore db;
    private CollectionReference bookingsCollection;

    private String patientUsername;

    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;

    private String patientsUsername;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_therapist);

        db = FirebaseFirestore.getInstance();
        bookingsCollection = db.collection("booking_appointments");

        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        therapyServiceTextView = findViewById(R.id.doctorServiceTextView);
        payAmountTextView = findViewById(R.id.payAmountTextView);
        datePickerButton = findViewById(R.id.datePickerButton);
        timeSlotsSpinner = findViewById(R.id.timeSlotsSpinner);
        bookButton = findViewById(R.id.bookButton);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);


        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        patientUsername = preferences.getString("username", null);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Patient User Details");

        usersRef.orderByChild("patientusername").equalTo(patientUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String firstName = snapshot.child("dataFirstName").getValue(String.class);
                    String lastName = snapshot.child("dataLastName").getValue(String.class);
                    String email = snapshot.child("dataEmail").getValue(String.class);
                    String patientusernames = snapshot.child("patientusername").getValue(String.class);

                    patientFirstName = firstName;
                    patientLastName = lastName;
                    patientEmail = email;
                    patientsUsername = patientusernames;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookingTherapistActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        String therapistName = intent.getStringExtra("therapistName");
        String therapyService = intent.getStringExtra("therapyService");
        String formattedPayment = intent.getStringExtra("formattedPayment");



        if (therapistName != null) {
            doctorNameTextView.setText(therapistName);
            therapyServiceTextView.setText(therapyService);
            payAmountTextView.setText(formattedPayment);
        }

        setupDatePickerButton();
        setupTimeSlotsSpinner();

        bookButton.setEnabled(false);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bookButton.isEnabled()) {
                    return;
                }

                String therapistName = doctorNameTextView.getText().toString();
                selectedDateStr = dateFormat.format(selectedDate.getTime());
                selectedTimeSlot = (Time) timeSlotsSpinner.getSelectedItem();
                String therapistEmail = getIntent().getStringExtra("therapistEmail");
                String patientRole = "Patient";

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");



                usersRef.orderByChild("role").equalTo(patientRole).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean bookingAdded = false;

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String patientUsername = userSnapshot.child("username").getValue(String.class);

                            if (patientUsername.equals(patientUsername)) {
                                String therapistUsername = getIntent().getStringExtra("therapistUsername");
                                String bookingId = UUID.randomUUID().toString();


                                bookingsCollection
                                        .whereEqualTo("therapist_username", therapistUsername)
                                        .whereEqualTo("date", selectedDateStr)
                                        .whereEqualTo("time_slot", selectedTimeSlot.toString())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            if (queryDocumentSnapshots.isEmpty()) {

                                                Map<String, Object> bookingData = new HashMap<>();
                                                bookingData.put("booking_id", bookingId);
                                                bookingData.put("therapist_username", therapistUsername);
                                                bookingData.put("patient_username", patientsUsername);
                                                bookingData.put("therapy_service", therapyService);
                                                bookingData.put("date", selectedDateStr);
                                                bookingData.put("time_slot", selectedTimeSlot.toString());
                                                bookingData.put("approved_status", "pending");
                                                bookingData.put("patient_first_name", patientFirstName);
                                                bookingData.put("patient_last_name", patientLastName);
                                                bookingData.put("patient_email", patientEmail);

                                                bookingsCollection.document(bookingId)
                                                        .set(bookingData, SetOptions.merge())
                                                        .addOnSuccessListener(aVoid -> {
                                                            bookButton.setEnabled(true);
                                                            sendEmailNotificationToTherapists(therapistEmail);
                                                            Toast.makeText(BookingTherapistActivity.this, "Booking successful", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> Toast.makeText(BookingTherapistActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                            } else {

                                                Toast.makeText(BookingTherapistActivity.this, "The Therapist is unavailable at this date and time slot", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {

                                            Toast.makeText(BookingTherapistActivity.this, "Error checking booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                                bookingAdded = true;
                                break;
                            }
                        }

                        if (!bookingAdded) {
                            Toast.makeText(BookingTherapistActivity.this, "Error: Patient not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BookingTherapistActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }




    private void setupDatePickerButton() {
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);

        Calendar maxDate = (Calendar) tomorrow.clone();
        maxDate.add(Calendar.DATE, 7);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateSelectedDateTextView();
                    bookButton.setEnabled(true);
                    setupTimeSlotsSpinner(); // Refresh time slots spinner when date is changed
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void updateSelectedDateTextView() {
        String formattedDate = dateFormat.format(selectedDate.getTime());
        selectedDateStr = formattedDate;
        selectedDateTextView.setText(formattedDate);
    }

    private void setupTimeSlotsSpinner() {
        Time[] timeSlots = {
                new Time(8, 0),
                new Time(10, 30),
                new Time(13, 30),
                new Time(16, 0),
                new Time(18, 30)
        };

        // Sort time slots
        Arrays.sort(timeSlots);

        // Convert Time objects to String representations
        ArrayList<Time> timeSlotsList = new ArrayList<>();
        for (Time timeSlot : timeSlots) {
            timeSlotsList.add(timeSlot);
        }

        ArrayAdapter<Time> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlotsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotsSpinner.setAdapter(adapter);
    }





    private void sendEmailNotificationToTherapists(String... therapistEmails) {
        new SendEmailTask().execute(therapistEmails);
    }

    private class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... therapistEmails) {
            for (String therapistEmail : therapistEmails) {
                sendEmailNotificationToTherapist(therapistEmail);
            }
            return null;
        }
    }



    private void sendEmailNotificationToTherapist(String therapistEmail) {
        final String senderEmail = "sender.email.107@gmail.com";
        final String password = "dcyxrnwmrwkbetvo";
        final String smtpHost = "smtp.gmail.com";
        final String smtpPort = "465";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", smtpPort);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(therapistEmail));
            String therapistName = doctorNameTextView.getText().toString();
            message.setSubject("Subject: Booking Notification for " + therapistName);
            message.setText("Dear " + therapistName + ",\n\n" +
                    "I hope this message finds you well. We wanted to inform you that a new booking has been made by one of your patients through Therapy Corner." +
                    "\n\nPatient Name: " + patientFirstName + " " + patientLastName +
                    "\nDate: " + dateFormat.format(selectedDate.getTime()) +
                    "\nTime: " + timeSlotsSpinner.getSelectedItem().toString() +
                    "\n\nPlease review the booking details and confirm the appointment with the patient accordingly." +
                    "\n\nThank you,\nThe Therapy Corner Team");

            Transport.send(message);
            System.out.println("Email sent successfully to therapist: " + therapistEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to therapist: " + therapistEmail);
        }
    }

    private class Time implements Comparable<Time> {
        private int hour;
        private int minute;

        public Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public int compareTo(Time other) {
            if (this.hour != other.hour) {
                return this.hour - other.hour;
            } else {
                return this.minute - other.minute;
            }
        }

        @Override
        public String toString() {
            return String.format("%02d:%02d", hour, minute);
        }
    }
}

