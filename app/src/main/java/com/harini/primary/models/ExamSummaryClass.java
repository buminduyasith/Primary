package com.harini.primary.models;

import com.harini.primary.adapters.SubjectMarks;

import java.util.List;

public class ExamSummaryClass {

    private String grade;
    private String term;
    private List<Hmarks> sinhalaHighestMark;
    private List<Hmarks> buddhismHighestMark;
    private List<Hmarks> englishHighestMark;
    private List<Hmarks> mathsHighestMark;
    private List<Hmarks> tamilHighestMark;
    private List<Hmarks> scienceHighestMark;

    public ExamSummaryClass(String grade, String term, List<Hmarks> sinhalaHighestMark, List<Hmarks> buddhismHighestMark, List<Hmarks> englishHighestMark, List<Hmarks> mathsHighestMark, List<Hmarks> tamilHighestMark, List<Hmarks> scienceHighestMark) {
        this.grade = grade;
        this.term = term;
        this.sinhalaHighestMark = sinhalaHighestMark;
        this.buddhismHighestMark = buddhismHighestMark;
        this.englishHighestMark = englishHighestMark;
        this.mathsHighestMark = mathsHighestMark;
        this.tamilHighestMark = tamilHighestMark;
        this.scienceHighestMark = scienceHighestMark;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ExamSummaryClass() {
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<Hmarks> getSinhalaHighestMark() {
        return sinhalaHighestMark;
    }

    public void setSinhalaHighestMark(List<Hmarks> sinhalaHighestMark) {
        this.sinhalaHighestMark = sinhalaHighestMark;
    }

    public List<Hmarks> getBuddhismHighestMark() {
        return buddhismHighestMark;
    }

    public void setBuddhismHighestMark(List<Hmarks> buddhismHighestMark) {
        this.buddhismHighestMark = buddhismHighestMark;
    }

    public List<Hmarks> getEnglishHighestMark() {
        return englishHighestMark;
    }

    public void setEnglishHighestMark(List<Hmarks> englishHighestMark) {
        this.englishHighestMark = englishHighestMark;
    }

    public List<Hmarks> getMathsHighestMark() {
        return mathsHighestMark;
    }

    public void setMathsHighestMark(List<Hmarks> mathsHighestMark) {
        this.mathsHighestMark = mathsHighestMark;
    }

    public List<Hmarks> getTamilHighestMark() {
        return tamilHighestMark;
    }

    public void setTamilHighestMark(List<Hmarks> tamilHighestMark) {
        this.tamilHighestMark = tamilHighestMark;
    }

    public List<Hmarks> getScienceHighestMark() {
        return scienceHighestMark;
    }

    public void setScienceHighestMark(List<Hmarks> scienceHighestMark) {
        this.scienceHighestMark = scienceHighestMark;
    }
}
