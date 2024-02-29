package com.example.finalproject;

public class MoodEntry {
    private long id;
    private String date;
    private String mood;
    private String reason;

    public MoodEntry() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public MoodEntry(long id, String date, String mood, String reason) {
        this.id = id;
        this.date = date;
        this.mood = mood;
        this.reason = reason;
    }


}

