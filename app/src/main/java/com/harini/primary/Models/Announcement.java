package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class Announcement {

    private String grade;

    private String useruid;

    private String message;

    private Timestamp timestamp;

    public Announcement() {
    }


    public Announcement(String grade, String useruid, String message, Timestamp timestamp) {
        this.grade = grade;
        this.useruid = useruid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}