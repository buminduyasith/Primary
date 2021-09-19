package com.harini.primary.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.Store;
import com.harini.primary.adapters.StudentListAdapter;
import com.harini.primary.adapters.SubjectMarks;
import com.harini.primary.models.StudentMarks;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StudentMarksViewP extends AppCompatActivity {

    private static final String TAG ="stdmarks" ;
    private RecyclerView recAddStudentMarks;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner spinner_grade;
    private SweetAlertDialog pDialog;


    private CollectionReference collectionReference;

    private StudentListAdapter adapter;
    private  TextInputLayout TIL_Buddhism_marks,TIL_sinhala_marks,TIL_maths_marks,TIL_english_marks,TIL_science_marks,TIL_tamil_marks;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks_view_p);
    //    setContentView(R.layout.activity_view_student_marks_t);
        setupUi();
        init();
        store  = Store.getInstance();
        getAllStudentMarksDetailsFromDB();

    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
       // collectionReference = db.collection("studentsmarks");




    }


    private int filtermarks(String marks){

        if(marks.isEmpty()|| marks==null||marks==""){
            return 0;
        }
        else{
            return Integer.valueOf(marks);
        }
    }

    private void setupUi() {

        TIL_Buddhism_marks = findViewById(R.id.TIL_Buddhism_marks);
        TIL_sinhala_marks = findViewById(R.id.TIL_sinhala_marks);
        TIL_maths_marks = findViewById(R.id.TIL_maths_marks);
        TIL_english_marks = findViewById(R.id.TIL_english_marks);
        TIL_science_marks = findViewById(R.id.TIL_science_marks);
        TIL_tamil_marks = findViewById(R.id.TIL_tamil_marks);
    }

    private void getAllStudentMarksDetailsFromDB(){
//        if(grade==null || grade.isEmpty()){
//            grade="3A";
//        }

       // String sid = mAuth.getCurrentUser().getUid();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

//        if(grade==null){
//            throw new RuntimeException("student grade should not be null");
//        }

        String sid = "AagVSWTS9lT8NhA5mFe79xn8yFb2";
        grade = "3A";



        db.collection("studentsmarks")
                .document(grade)
                .collection("Students")
                .whereEqualTo("studentId",sid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<StudentMarks> studentMarksList =  queryDocumentSnapshots.toObjects(StudentMarks.class);

                        if(studentMarksList==null) throw  new NullPointerException("studentmarksList null");

                        StudentMarks studentMarks = studentMarksList.get(0);

                        if(studentMarks==null) throw  new NullPointerException("studentMarks null");

                        List<SubjectMarks> SubjectMarksList = studentMarks.getStudentSubjectMarksList();

                        Log.d(TAG, "onSuccess: "+studentMarksList.toString());

                        for (SubjectMarks subjectm:SubjectMarksList){
                            switch(subjectm.getSubject()) {
                                case SINHALA:
                                    // code block
                                    TIL_sinhala_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    Log.d(TAG, "openDialog: sinhalatil"+TIL_sinhala_marks.getEditText().getText().toString());
                                    break;
                                case ENGLISH:
                                    TIL_maths_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    break;
                                case MATHS:
                                    TIL_english_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    break;
                                case SCIENCE:
                                    TIL_science_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    break;
                                case TAMIL:
                                    TIL_tamil_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    break;
                                case BUDDHISM:
                                    TIL_Buddhism_marks.getEditText().setText(String.valueOf(subjectm.getMarks()));
                                    break;
                                default:
                                    return;
                                // code block
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,"fail"+ e.getLocalizedMessage() );
            }
        });

    }

}