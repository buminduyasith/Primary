package com.harini.primary.models;

import com.google.firebase.Timestamp;

public class CustomeEvent {

    private String discription;

    private Timestamp timestamp;

    private String date;

    public CustomeEvent() {
    }


    public CustomeEvent(String discription, Timestamp timestamp, String date) {
        this.discription = discription;
        this.timestamp = timestamp;
        this.date = date;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


