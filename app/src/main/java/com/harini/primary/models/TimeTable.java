package com.harini.primary.models;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<Period> monday;
    private ArrayList<Period> tuesday;
    private ArrayList<Period> wednesday;
    private ArrayList<Period> thursday;
    private ArrayList<Period> friday;

    public ArrayList<Period> getMonday() {
        return monday;
    }

    public void setMonday(ArrayList<Period> monday) {
        this.monday = monday;
    }

    public ArrayList<Period> getTuesday() {
        return tuesday;
    }

    public void setTuesday(ArrayList<Period> tuesday) {
        this.tuesday = tuesday;
    }

    public ArrayList<Period> getWednesday() {
        return wednesday;
    }

    public void setWednesday(ArrayList<Period> wednesday) {
        this.wednesday = wednesday;
    }

    public ArrayList<Period> getThursday() {
        return thursday;
    }

    public void setThursday(ArrayList<Period> thursday) {
        this.thursday = thursday;
    }

    public ArrayList<Period> getFriday() {
        return friday;
    }

    public void setFriday(ArrayList<Period> friday) {
        this.friday = friday;
    }
}
