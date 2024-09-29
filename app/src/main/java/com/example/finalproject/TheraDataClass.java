package com.example.finalproject;


import java.io.Serializable;

public class TheraDataClass implements Serializable {

    private String username;

    private String therapistusername;

    private double theradataMonthlyPay;
    private String theradataFirstName;
    private String theradataLastName;
    private String theradataEmail;
    private String theradataPhone;
    private String theradataImage;
    private String theradataGender;
    private String theradataOfficeAddress;
    private String theradataArea;
    private String theradataTherapyServices;
    private String theradataOfferedLanguages;
    private String theradataServicesPlatforms;

    private String theradataFullName;

//    private String theradataCommunicationMediums;


    public String getUsername() {
        return username;
    }

    public String getTherapistusername() {
        return therapistusername;
    }

    public String getTheradataFirstName() {
        return theradataFirstName;
    }

    public String getTheradataFullName () {

        return  theradataFullName;
    }



    public String getTheradataLastName() {
        return theradataLastName;
    }

    public String getTheradataEmail() {
        return theradataEmail;
    }

    public String getTheradataPhone() {
        return theradataPhone;
    }

    public String getTheradataImage() {
        return theradataImage;
    }

    public String getTheradataGender() {
        return theradataGender;
    }

    public String getTheradataOfficeAddress() {
        return theradataOfficeAddress;
    }

    public String getTheradataArea() {
        return theradataArea;
    }

    public String getTheradataTherapyServices() {
        return theradataTherapyServices;
    }

    public String getTheradataOfferedLanguages() {
        return theradataOfferedLanguages;
    }

    public String getTheradataServicesPlatforms() {
        return theradataServicesPlatforms;
    }

    public double getTheradataMonthlyPay() { return theradataMonthlyPay; }

    //    public String getTheradataCommunicationMediums() {
//        return theradataCommunicationMediums;
//    }


    public TheraDataClass(String theradataFirstName, String theradataLastName, String theradataEmail, String theradataPhone,
                          String theradataImage, String theradataGender, String theradataOfficeAddress, String theradataArea,
                          String theradataTherapyServices, String theradataOfferedLanguages,
                          String theradataServicesPlatforms, double theradataMonthlyPay )
    {
        this.theradataFirstName = theradataFirstName;
        this.theradataLastName = theradataLastName;
        this.theradataEmail = theradataEmail;
        this.theradataPhone = theradataPhone;
        this.theradataImage = theradataImage;
        this.theradataGender = theradataGender;
        this.theradataOfficeAddress = theradataOfficeAddress;
        this.theradataArea = theradataArea;
        this.theradataTherapyServices = theradataTherapyServices;
        this.theradataOfferedLanguages = theradataOfferedLanguages;
        this.theradataServicesPlatforms = theradataServicesPlatforms;
        this.theradataMonthlyPay = theradataMonthlyPay;

    }

    public TheraDataClass() {

    }

    public void  setTheradataFullName (String theradataFullName) {
        this.theradataFullName = theradataFullName;
    }

    public void setTherapistusername(String therapistusername) {
        this.therapistusername = therapistusername;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTheradataMonthlyPay(double theradataMonthlyPay) {
        this.theradataMonthlyPay = theradataMonthlyPay;
    }

    public void setTheradataFirstName(String theradataFirstName) {
        this.theradataFirstName = theradataFirstName;
    }

    public void setTheradataLastName(String theradataLastName) {
        this.theradataLastName = theradataLastName;
    }

    public void setTheradataEmail(String theradataEmail) {
        this.theradataEmail = theradataEmail;
    }

    public void setTheradataPhone(String theradataPhone) {
        this.theradataPhone = theradataPhone;
    }

    public void setTheradataImage(String theradataImage) {
        this.theradataImage = theradataImage;
    }

    public void setTheradataGender(String theradataGender) {
        this.theradataGender = theradataGender;
    }

    public void setTheradataOfficeAddress(String theradataOfficeAddress) {
        this.theradataOfficeAddress = theradataOfficeAddress;
    }

    public void setTheradataArea(String theradataArea) {
        this.theradataArea = theradataArea;
    }

    public void setTheradataTherapyServices(String theradataTherapyServices) {
        this.theradataTherapyServices = theradataTherapyServices;
    }

    public void setTheradataOfferedLanguages(String theradataOfferedLanguages) {
        this.theradataOfferedLanguages = theradataOfferedLanguages;
    }

    public void setTheradataServicesPlatforms(String theradataServicesPlatforms) {
        this.theradataServicesPlatforms = theradataServicesPlatforms;
    }
}