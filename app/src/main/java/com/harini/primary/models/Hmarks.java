package com.harini.primary.models;

public class Hmarks {

    private String name;
    private String sid;
    private SubjectNameEnum Subject;
    private int marks;

    public Hmarks() {
    }

    public Hmarks(String name, String sid, SubjectNameEnum subject, int marks) {
        this.name = name;
        this.sid = sid;
        Subject = subject;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public SubjectNameEnum getSubject() {
        return Subject;
    }

    public void setSubject(SubjectNameEnum subject) {
        Subject = subject;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
