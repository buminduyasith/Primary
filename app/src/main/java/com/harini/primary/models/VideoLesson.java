package com.harini.primary.models;

import com.google.firebase.Timestamp;

public class VideoLesson {


    private String title;

    private Timestamp timestamp;

    private String videoid;

    public VideoLesson() {
    }


    public VideoLesson(String title, Timestamp timestamp, String videoid) {
        this.title = title;
        this.timestamp = timestamp;
        this.videoid = videoid;
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

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}
