package com.harini.primary.models;

import java.util.List;

public class ExamDetails {

    private List<StudentMarks> studentMarksList;
    private String subject;


    public ExamDetails(List<StudentMarks> studentMarksList, String subject) {
        this.studentMarksList = studentMarksList;
        this.subject = subject;

    }

    public List<StudentMarks> getStudentMarksList() {
        return studentMarksList;
    }

    public void setStudentMarksList(List<StudentMarks> studentMarksList) {
        this.studentMarksList = studentMarksList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "ExamDetails{" +
                "studentMarksList=" + studentMarksList.toString() +
                ", subject='" + subject + '\'' +
                '}';
    }
}
