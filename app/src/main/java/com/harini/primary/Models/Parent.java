package com.harini.primary.Models;

public class Parent {

    private String firstName;

    private String grade;

    private String lastName;

    private String phoneNumber;

    private String registerID;

    private String studentName;


    public Parent() {
    }

    public Parent(String firstName, String grade, String lastName, String phoneNumber, String registerID, String studentName) {
        this.firstName = firstName;
        this.grade = grade;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.registerID = registerID;
        this.studentName = studentName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegisterID() {
        return registerID;
    }

    public void setRegisterID(String registerID) {
        this.registerID = registerID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
