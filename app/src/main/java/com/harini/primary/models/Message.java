package com.harini.primary.models;

import com.google.firebase.Timestamp;

public class Message {

    private String msg;
    private String senderRole;
    private Timestamp timestamp;
    private String senderName;

    public Message() {
    }

    public Message(String msg, String senderRole, String senderName,Timestamp timestamp) {
        this.msg = msg;
        this.senderRole = senderRole;
        this.timestamp = timestamp;
        this.senderName = senderName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
