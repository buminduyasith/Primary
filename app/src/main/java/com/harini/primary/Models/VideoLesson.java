package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class VideoLesson {


    private String title;

    private Timestamp timestamp;

    public VideoLesson() {
    }

    public VideoLesson(String title, Timestamp timestamp) {
        this.title = title;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
