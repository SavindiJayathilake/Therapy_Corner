package com.example.finalproject;

public class Appointment {

    private String patient_first_name;

    private String patient_last_name;

    private String patient_email;

    private String date;

    private String time_slot;

    private String booking_id;

    private String patient_username;

    private String therapist_username;

    private String time;

    private String therapist_name;

    private String approved_status;

    private String therapy_service;


    public Appointment() {}
    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public Appointment(String patient_username, String date, String time_slot) {
        this.patient_username = patient_username;
        this.date = date;
        this.time_slot = time_slot;
    }

    public String getTherapy_service() {
        return therapy_service;
    }

    public void setTherapy_service(String therapy_service) {
        this.therapy_service = therapy_service;
    }

    public String getPatient_username() {
        return patient_username;
    }

    public void setPatient_username(String patient_username) {
        this.patient_username = patient_username;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient_first_name() {
        return patient_first_name;
    }

    public void setPatient_first_name(String patient_first_name) {
        this.patient_first_name = patient_first_name;
    }

    public String getPatient_last_name() {
        return patient_last_name;
    }

    public void setPatient_last_name(String patient_last_name) {
        this.patient_last_name = patient_last_name;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTherapist_name() {
        return therapist_name;
    }

    public void setTherapist_name(String therapist_name) {
        this.therapist_name = therapist_name;
    }

    public String getTherapist_username() {
        return therapist_username;
    }

    public void setTherapist_username(String therapist_username) {
        this.therapist_username = therapist_username;
    }
}
