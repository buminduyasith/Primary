package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class Homework {

    private String discription;

    private Timestamp timestamp;

    private String link;

    private String stdclass;

    public Homework() {
    }

    public Homework(String discription, Timestamp timestamp, String link,String stdclass) {
        this.discription = discription;
        this.timestamp = timestamp;
        this.link = link;
        this.stdclass = stdclass;
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

    public String getStdclass() {
        return stdclass;
    }

    public void setStdclass(String stdclass) {
        this.stdclass = stdclass;
    }
}
