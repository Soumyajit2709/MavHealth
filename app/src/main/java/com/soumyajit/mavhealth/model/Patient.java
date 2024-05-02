package com.soumyajit.mavhealth.model;

public class Patient {
    private String name;
    private String address;
    private String tel;
    private String email;
    private String dob;
    private String gender;


    public Patient(){
        //needed for firebase
    }

    public Patient(String name, String address, String tel, String email, String dob, String gender) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}