package com.harini.primary.models;

public class Teacher {

    private String firstName;

    private String lastName;

    private String grade;

    private String phoneNumber;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String grade, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
