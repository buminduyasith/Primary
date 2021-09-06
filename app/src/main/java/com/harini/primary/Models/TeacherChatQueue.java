package com.harini.primary.Models;

import com.google.firebase.Timestamp;

public class TeacherChatQueue {

    private String grade;
    private String studentId;
    private String teacherid;
    private String name;
    private String recentMsg;
    private Timestamp timestamp;

    public TeacherChatQueue() {
    }

    public TeacherChatQueue(String grade, String studentId, String teacherid, String name, String recentMsg, Timestamp timestamp) {
        this.grade = grade;
        this.studentId = studentId;
        this.teacherid = teacherid;
        this.name = name;
        this.recentMsg = recentMsg;
        this.timestamp = timestamp;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
