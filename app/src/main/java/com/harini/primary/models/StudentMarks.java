package com.harini.primary.models;

import com.harini.primary.adapters.SubjectMarks;

import java.util.List;

public class StudentMarks {

    private String StudentId;
    private List<SubjectMarks> StudentSubjectMarksList;
    private String name;

    public StudentMarks() {
    }

    public StudentMarks(String studentId, List<SubjectMarks> studentSubjectMarksList, String name) {
        StudentId = studentId;
        StudentSubjectMarksList = studentSubjectMarksList;
        this.name = name;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public List<SubjectMarks> getStudentSubjectMarksList() {
        return StudentSubjectMarksList;
    }

    public void setStudentSubjectMarksList(List<SubjectMarks> studentSubjectMarksList) {
        StudentSubjectMarksList = studentSubjectMarksList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentMarks{" +
                "StudentId='" + StudentId + '\'' +
                ", StudentSubjectMarksList=" + StudentSubjectMarksList +
                ", name='" + name + '\'' +
                '}';
    }
}
