package com.harini.primary.models;

import com.google.firebase.Timestamp;

public class ExamPaper {

    private String filename;

    private Timestamp timestamp;

    private String link;

    private String stdclass;

    private String subject;

    private String uid;

    public ExamPaper() {
    }

    public ExamPaper(String filename, Timestamp timestamp, String link, String stdclass, String subject, String uid) {
        this.filename = filename;
        this.timestamp = timestamp;
        this.link = link;
        this.stdclass = stdclass;
        this.subject = subject;
        this.uid = uid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
