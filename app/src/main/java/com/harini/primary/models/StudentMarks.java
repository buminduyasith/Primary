package com.harini.primary.models;

import com.harini.primary.adapters.SubjectMarks;

import java.util.List;

public class StudentMarks {

    private String StudentId;
    private List<SubjectMarks> StudentSubjectMarksList;
    private String name;
    private String term;

    public StudentMarks() {
    }

    public StudentMarks(String studentId, List<SubjectMarks> studentSubjectMarksList, String name, String term) {
        StudentId = studentId;
        StudentSubjectMarksList = studentSubjectMarksList;
        this.name = name;
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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
