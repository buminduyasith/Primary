package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class Message {

    private String msg;
    private String senderRole;
    private Timestamp timestamp;

    public Message() {
    }

    public Message(String msg, String senderRole, Timestamp timestamp) {
        this.msg = msg;
        this.senderRole = senderRole;
        this.timestamp = timestamp;
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
}
