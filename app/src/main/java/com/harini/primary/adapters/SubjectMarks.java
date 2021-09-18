package com.harini.primary.adapters;

import com.harini.primary.models.SubjectNameEnum;

public class SubjectMarks {

    private SubjectNameEnum subject;
    private int marks;

    public SubjectMarks() {
    }

    public SubjectMarks(SubjectNameEnum subject, int marks) {
        this.subject = subject;
        this.marks = marks;
    }

    public SubjectNameEnum getSubject() {
        return subject;
    }

    public void setSubject(SubjectNameEnum subject) {
        this.subject = subject;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "SubjectMarks{" +
                "subject=" + subject +
                ", marks=" + marks +
                '}';
    }
}
