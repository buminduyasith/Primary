package com.harini.primary;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.harini.primary.adapters.SubjectMarks;
import com.harini.primary.models.ExamSummaryClass;
import com.harini.primary.models.Hmarks;
import com.harini.primary.models.StudentExamDetail;
import com.harini.primary.models.StudentMarks;
import com.harini.primary.models.SubjectNameEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.harini.primary.models.SubjectNameEnum.BUDDHISM;
import static com.harini.primary.models.SubjectNameEnum.ENGLISH;
import static com.harini.primary.models.SubjectNameEnum.MATHS;
import static com.harini.primary.models.SubjectNameEnum.SCIENCE;
import static com.harini.primary.models.SubjectNameEnum.SINHALA;
import static com.harini.primary.models.SubjectNameEnum.TAMIL;
import static java.util.stream.Collectors.groupingBy;

public final class Store {
    private static Store instance;
    public String value;
    private static List<StudentMarks> AllStudentMarksDetails;
    private static final String TAG ="storev2" ;

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
        StudentMarks studentMarksdb = getSpecificStudentMarks(studentMarks.getStudentId(),studentMarks.getTerm());
        if(studentMarksdb!=null){
            int pos = AllStudentMarksDetails.indexOf(studentMarksdb);
            AllStudentMarksDetails.set(pos,studentMarks);
        }
        AllStudentMarksDetails.add(studentMarks);
    }

    public StudentMarks getSpecificStudentMarks(String id,String term){

        for (StudentMarks studentMarks: AllStudentMarksDetails){

            String stdid = studentMarks.getStudentId();
            String term1 = studentMarks.getTerm();
            Log.d(TAG, "getSpecificStudentMarks: "+ studentMarks.getTerm());
            if(stdid.contains(id) && term.contains(term1)){
                return studentMarks;
            }
        }

        return null;
    }

    public void StoreInitDB(List<StudentMarks> getDBAllStudentMarksDetails){

        AllStudentMarksDetails = getDBAllStudentMarksDetails;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ExamSummaryClass summaryGenerate(){

        List<StudentExamDetail> studentExamDetailArrayList =new ArrayList<>();
        int total = 0;
       for (StudentMarks studentMarks:AllStudentMarksDetails){

           StudentExamDetail studentExamDetail = new StudentExamDetail();

         //  Log.d(TAG, "student id: "+studentMarks.getName());
           for (SubjectMarks subjectMarks: studentMarks.getStudentSubjectMarksList()){

               total += subjectMarks.getMarks();
               Log.d(TAG, "summaryGenerate: "+subjectMarks.toString());
               switch(subjectMarks.getSubject()) {
                   case SINHALA:
                       // code block
                        studentExamDetail.setSINHALAMarks(subjectMarks.getMarks());
                       break;
                   case ENGLISH:
                       Log.d(TAG, "summaryGenerate: -i english"+subjectMarks.getMarks());
                       studentExamDetail.setENGLISHMarks(subjectMarks.getMarks());
                       break;
                   case MATHS:
                       studentExamDetail.setMATHSMarks(subjectMarks.getMarks());
                       break;
                   case SCIENCE:
                       studentExamDetail.setSCIENCEMarks(subjectMarks.getMarks());
                       break;
                   case TAMIL:
                       studentExamDetail.setTAMILMarks(subjectMarks.getMarks());
                       break;
                   case BUDDHISM:
                       studentExamDetail.setBUDDHISMMarks(subjectMarks.getMarks());
                       break;
                   default:
                      // return;
                   // code block
               }


           }
           float avg = total/6;
           Log.d(TAG, "total marks: "+total);

           studentExamDetail.setName(studentMarks.getName());
           studentExamDetail.setId(studentMarks.getStudentId());
           studentExamDetail.setTotalMarks(total);
           studentExamDetail.setAvg(avg);
           studentExamDetailArrayList.add(studentExamDetail);
           total =0;
           Log.d(TAG, "avg: "+avg);
           Log.e(TAG, "---------------------------------");
       }

        Log.d(TAG, "summaryGenerate: studentExamDetailArrayList"+studentExamDetailArrayList.toString());


        ExamSummaryClass examSummaryClass = new ExamSummaryClass();
        List<StudentExamDetail>  highestMarksForSinhala = getHighestMarksForSinhala(studentExamDetailArrayList);
        List<StudentExamDetail>  highestMarksForBuddhism = getHighestMarksForBuddhism(studentExamDetailArrayList);
        List<StudentExamDetail>  highestMarksForEnglish = getHighestMarksForEnglish(studentExamDetailArrayList);
        List<StudentExamDetail>  highestMarksForScience = getHighestMarksForScience(studentExamDetailArrayList);
        List<StudentExamDetail>  highestMarksForTamil = getHighestMarksForTamil(studentExamDetailArrayList);
        List<StudentExamDetail>  highestMarksForMaths = getHighestMarksForMaths(studentExamDetailArrayList);



        List<Hmarks> sinhalaHmarks = new ArrayList<>();
        List<Hmarks> englishHmarks = new ArrayList<>();
        List<Hmarks> buddhismHmarks = new ArrayList<>();
        List<Hmarks> scienceHmarks = new ArrayList<>();
        List<Hmarks> tamilHmarks = new ArrayList<>();
        List<Hmarks> mathsHmarks = new ArrayList<>();


        for(StudentExamDetail sample:highestMarksForSinhala){
            sinhalaHmarks.add(new Hmarks(sample.getName(),sample.getId(), SINHALA,sample.getSINHALAMarks()));

        }

        for(StudentExamDetail sample:highestMarksForBuddhism){
            buddhismHmarks.add(new Hmarks(sample.getName(),sample.getId(), BUDDHISM,sample.getBUDDHISMMarks()));

        }


        for(StudentExamDetail sample:highestMarksForEnglish){
            englishHmarks.add(new Hmarks(sample.getName(),sample.getId(), ENGLISH,sample.getENGLISHMarks()));

        }


        for(StudentExamDetail sample:highestMarksForScience){
            scienceHmarks.add(new Hmarks(sample.getName(),sample.getId(), SCIENCE,sample.getSCIENCEMarks()));

        }

        for(StudentExamDetail sample:highestMarksForTamil){
            tamilHmarks.add(new Hmarks(sample.getName(),sample.getId(), TAMIL,sample.getTAMILMarks()));

        }

        for(StudentExamDetail sample:highestMarksForMaths){
            mathsHmarks.add(new Hmarks(sample.getName(),sample.getId(), MATHS,sample.getMATHSMarks()));

        }

        examSummaryClass.setSinhalaHighestMark(sinhalaHmarks);
        examSummaryClass.setBuddhismHighestMark(buddhismHmarks);
        examSummaryClass.setEnglishHighestMark(englishHmarks);
        examSummaryClass.setScienceHighestMark(scienceHmarks);
        examSummaryClass.setTamilHighestMark(tamilHmarks);
        examSummaryClass.setMathsHighestMark(mathsHmarks);

        return examSummaryClass;

       // buddhismHmarks.stream()
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForSinhala(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getSINHALAMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
        return value;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForMaths(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getMATHSMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
        return value;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForBuddhism(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getBUDDHISMMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()

                .getValue();
        Log.d(TAG, "getHighestMarksForBuddhism: "+value.toString());
        return value;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForEnglish(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getENGLISHMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
        return value;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForScience(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getSCIENCEMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
        return value;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<StudentExamDetail> getHighestMarksForTamil(List<StudentExamDetail> list){

        List<StudentExamDetail> value = list.stream()
                .filter( distinctByKey(p -> p.getId()))
                .collect(groupingBy(StudentExamDetail::getTAMILMarks))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
        return value;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}