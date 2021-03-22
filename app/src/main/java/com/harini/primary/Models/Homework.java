package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class Homework {

    private String discription;

    private Timestamp timestamp;

    private String link;

    public Homework() {
    }

    public Homework(String discription, Timestamp timestamp, String link) {
        this.discription = discription;
        this.timestamp = timestamp;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
