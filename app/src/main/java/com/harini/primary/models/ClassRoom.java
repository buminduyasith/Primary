package com.harini.primary.models;

public class ClassRoom {
    private String className; // ex - 3-A
    private TimeTable timeTable;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
