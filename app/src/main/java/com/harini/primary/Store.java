package com.harini.primary;


import com.harini.primary.models.StudentMarks;

import java.util.ArrayList;
import java.util.List;

public final class Store {
    private static Store instance;
    public String value;
    private static List<StudentMarks> AllStudentMarksDetails;

    private Store() {
        // The following code emulates slow initialization.
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }

        AllStudentMarksDetails = new ArrayList<>();
    }

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }
    public  List<StudentMarks>  getAllStudentMarksDetails(){
        return AllStudentMarksDetails;
    }
    public  void addStudentMark(StudentMarks studentMarks){
        StudentMarks studentMarksdb = getSpecificStudentMarks(studentMarks.getStudentId());
        if(studentMarksdb!=null){
            int pos = AllStudentMarksDetails.indexOf(studentMarksdb);
            AllStudentMarksDetails.set(pos,studentMarks);
        }
        AllStudentMarksDetails.add(studentMarks);
    }

    public StudentMarks getSpecificStudentMarks(String id){

        for (StudentMarks studentMarks: AllStudentMarksDetails){

            String stdid = studentMarks.getStudentId();
            if(stdid.contains(id)){
                return studentMarks;
            }
        }

        return null;
    }

    public void StoreInitDB(List<StudentMarks> getDBAllStudentMarksDetails){

        AllStudentMarksDetails = getDBAllStudentMarksDetails;
    }


}