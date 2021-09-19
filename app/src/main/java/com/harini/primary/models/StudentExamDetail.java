package com.harini.primary.models;

public class StudentExamDetail {

    private String name;
    private String id;
    private int totalMarks;
    private float avg;
    private int place;
    private int SINHALAMarks;
    private int MATHSMarks;
    private int SCIENCEMarks;
    private int ENGLISHMarks;
    private int TAMILMarks;
    private int BUDDHISMMarks;
    private int term;

    public StudentExamDetail() {
    }

    public StudentExamDetail(String name, String id, int totalMarks, float avg, int place, int SINHALAMarks, int MATHSMarks, int SCIENCEMarks, int ENGLISHMarks, int TAMILMarks, int BUDDHISMMarks, int term) {
        this.name = name;
        this.id = id;
        this.totalMarks = totalMarks;
        this.avg = avg;
        this.place = place;
        this.SINHALAMarks = SINHALAMarks;
        this.MATHSMarks = MATHSMarks;
        this.SCIENCEMarks = SCIENCEMarks;
        this.ENGLISHMarks = ENGLISHMarks;
        this.TAMILMarks = TAMILMarks;
        this.BUDDHISMMarks = BUDDHISMMarks;
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public float getAvg() {
        return avg;
    }

    public int getPlace() {
        return place;
    }

    public int getSINHALAMarks() {
        return SINHALAMarks;
    }

    public int getMATHSMarks() {
        return MATHSMarks;
    }

    public int getSCIENCEMarks() {
        return SCIENCEMarks;
    }

    public int getENGLISHMarks() {
        return ENGLISHMarks;
    }

    public int getTAMILMarks() {
        return TAMILMarks;
    }

    public int getBUDDHISMMarks() {
        return BUDDHISMMarks;
    }

    public int getTerm() {
        return term;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void setSINHALAMarks(int SINHALAMarks) {
        this.SINHALAMarks = SINHALAMarks;
    }

    public void setMATHSMarks(int MATHSMarks) {
        this.MATHSMarks = MATHSMarks;
    }

    public void setSCIENCEMarks(int SCIENCEMarks) {
        this.SCIENCEMarks = SCIENCEMarks;
    }

    public void setENGLISHMarks(int ENGLISHMarks) {
        this.ENGLISHMarks = ENGLISHMarks;
    }

    public void setTAMILMarks(int TAMILMarks) {
        this.TAMILMarks = TAMILMarks;
    }

    public void setBUDDHISMMarks(int BUDDHISMMarks) {
        this.BUDDHISMMarks = BUDDHISMMarks;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "StudentExamDetail{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", totalMarks=" + totalMarks +
                ", avg=" + avg +
                ", place=" + place +
                ", SINHALAMarks=" + SINHALAMarks +
                ", MATHSMarks=" + MATHSMarks +
                ", SCIENCEMarks=" + SCIENCEMarks +
                ", ENGLISHMarks=" + ENGLISHMarks +
                ", TAMILMarks=" + TAMILMarks +
                ", BUDDHISMMarks=" + BUDDHISMMarks +
                ", term=" + term +
                '}';
    }
}
