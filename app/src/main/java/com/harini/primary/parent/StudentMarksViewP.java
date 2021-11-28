package com.harini.primary.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.harini.primary.teacher.ViewStudentMarksT;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StudentMarksViewP extends AppCompatActivity {

    private static final String TAG ="stdmarks" ;
    private RecyclerView recAddStudentMarks;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner spinner_term;
    private SweetAlertDialog pDialog;

    private List<String> terms;

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
        //getAllStudentMarksDetailsFromDB();

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
        spinner_term = findViewById(R.id.spinner_term);
        terms = new ArrayList<>();
        terms.add("1st");
        terms.add("2nd");
        terms.add("3rd");

        ArrayAdapter<String> termsAdapteer = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                terms

        );

        spinner_term.setAdapter(termsAdapteer);

        spinner_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String term = "1st";
                if( spinner_term.getSelectedItem()!=null){
                    Log.d(TAG, "onItemSelected: if  term"+term);
                    term = spinner_term.getSelectedItem().toString();
                }
                else{
                    Log.d(TAG, "onItemSelected: else  term"+term);

                }

                getAllStudentMarksDetailsFromDB(term);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getAllStudentMarksDetailsFromDB(String term){
//        if(grade==null || grade.isEmpty()){
//            grade="3A";
//        }
        pDialog = new SweetAlertDialog(StudentMarksViewP.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("please wait exam summary creating...");
        //pDialog.setContentText("All marks added successfully..");
        pDialog.show();

        String sid = mAuth.getCurrentUser().getUid();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException("student grade should not be null");
        }

        //String sid = "AagVSWTS9lT8NhA5mFe79xn8yFb2";
       // grade = "3A";



        db.collection("studentsmarks")
                .document(grade)
                .collection("Students")
                .whereEqualTo("studentId",sid)
                .whereEqualTo("term",term)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<StudentMarks> studentMarksList =  queryDocumentSnapshots.toObjects(StudentMarks.class);

                        if(studentMarksList==null || studentMarksList.isEmpty()) {
                            pDialog.dismissWithAnimation();
                            Toast.makeText(getApplicationContext(),"not available",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        StudentMarks studentMarks = studentMarksList.get(0);

                        if(studentMarks==null) throw  new NullPointerException("studentMarks null");

                        List<SubjectMarks> SubjectMarksList = studentMarks.getStudentSubjectMarksList();

                        Log.d(TAG, "onSuccess: "+studentMarksList.toString());

                        for (SubjectMarks subjectm:SubjectMarksList){
                            switch(subjectm.getSubject()) {
                                case SINHALA:
                                    // code block
                                    TIL_sinhala_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    Log.d(TAG, "openDialog: sinhalatil"+TIL_sinhala_marks.getEditText().getText().toString());
                                    break;
                                case ENGLISH:
                                    TIL_maths_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    break;
                                case MATHS:
                                    TIL_english_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    break;
                                case SCIENCE:
                                    TIL_science_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    break;
                                case TAMIL:
                                    TIL_tamil_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    break;
                                case BUDDHISM:
                                    TIL_Buddhism_marks.getEditText().setText(marksformat(subjectm.getMarks()));
                                    break;
                                default:
                                    return;
                                // code block
                            }
                        }

                        pDialog.dismissWithAnimation();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,"fail"+ e.getLocalizedMessage() );
                pDialog.dismissWithAnimation();
            }
        });

    }



    private String marksformat(int marks){

        if(marks==0){
            return "";
        }
        else{
            return String.valueOf(marks);
        }
    }

}