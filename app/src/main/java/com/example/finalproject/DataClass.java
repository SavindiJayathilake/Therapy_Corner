package com.example.finalproject;

public class DataClass {

    private String username;
    private String dataFirstName;
    private String dataLastName;
    private String dataEmail;
    private String dataPhone;
    private String dataImage;


    private String patientusername;


    public String getPatientusername() {return patientusername;}

    public void setPatientusername(String patientusername) {
        this.patientusername = patientusername;
    }

    public String getDataFirstName() {
        return dataFirstName;
    }

    public String getDataLastName() {
        return dataLastName;
    }

    public String getDataEmail() {
        return dataEmail;
    }

    public String getDataPhone() {
        return dataPhone;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataFirstName, String dataLastName, String dataEmail, String dataPhone, String dataImage) {
        this.dataFirstName = dataFirstName;
        this.dataLastName = dataLastName;
        this.dataEmail = dataEmail;
        this.dataPhone = dataPhone;
        this.dataImage = dataImage;
    }
    public DataClass(){
    }
}