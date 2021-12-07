package com.harini.primary.models;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

public class ParentsList {

    private String firstName;

    private String lastName;
    private String studentName;
    private String registerID;
    private String grade;




    public ParentsList() {
    }

    public ParentsList(String firstName, String lastName,String studentName,String registerID,String grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentName = studentName;
        this.registerID=registerID;

    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getStudentName() {
        return studentName;
    }


    public String getregisterID() {
        return registerID;
    }

    public String getgrade() {
        return grade;
    }


}
