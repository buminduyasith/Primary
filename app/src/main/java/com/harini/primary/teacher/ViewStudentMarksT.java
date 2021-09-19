package com.harini.primary.teacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.R;
import com.harini.primary.Store;
import com.harini.primary.adapters.StudentListAdapter;
import com.harini.primary.adapters.SubjectMarks;
import com.harini.primary.admin.AddStudentMarks;
import com.harini.primary.models.ExamDetails;
import com.harini.primary.models.Parent;
import com.harini.primary.models.StudentMarks;
import com.harini.primary.models.SubjectNameEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.harini.primary.models.SubjectNameEnum.BUDDHISM;
import static com.harini.primary.models.SubjectNameEnum.ENGLISH;
import static com.harini.primary.models.SubjectNameEnum.MATHS;
import static com.harini.primary.models.SubjectNameEnum.SCIENCE;
import static com.harini.primary.models.SubjectNameEnum.SINHALA;
import static com.harini.primary.models.SubjectNameEnum.TAMIL;

public class ViewStudentMarksT extends AppCompatActivity {
    private static final String TAG ="exammarks" ;
    private RecyclerView recAddStudentMarks;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner spinner_grade;
    private SweetAlertDialog pDialog;


    private CollectionReference collectionReference;

    private StudentListAdapter adapter;
    private Store store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_marks_t);
        setupUi();
        init();
        store  = Store.getInstance();
        setupRecycleView();
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Parents");

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

        recAddStudentMarks = findViewById(R.id.recAddStudentMarks);
    }
    private void openDialog(String name,String studentId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudentMarksT.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_marks, null);


        final TextInputLayout TIL_Buddhism_marks = view.findViewById(R.id.TIL_Buddhism_marks);
        final TextInputLayout TIL_sinhala_Marks = view.findViewById(R.id.TIL_sinhala_Marks);
        final TextInputLayout TIL_maths_marks = view.findViewById(R.id.TIL_maths_marks);
        final TextInputLayout TIL_english_marks = view.findViewById(R.id.TIL_english_marks);
        final TextInputLayout TIL_science_marks = view.findViewById(R.id.TIL_science_marks);
        final TextInputLayout TIL_tamil_marks = view.findViewById(R.id.TIL_tamil_marks);
        final TextView txtStudentName = view.findViewById(R.id.txtStudentName);

        TIL_Buddhism_marks.setEnabled(false);
        TIL_sinhala_Marks.setEnabled(false);
        TIL_maths_marks.setEnabled(false);
        TIL_english_marks.setEnabled(false);
        TIL_science_marks.setEnabled(false);
        TIL_tamil_marks.setEnabled(false);
        txtStudentName.setText(name);


        StudentMarks fill_studentMarks = store.getSpecificStudentMarks(studentId);


        if(fill_studentMarks!=null){
            List<SubjectMarks> fill_subjectMarks = fill_studentMarks.getStudentSubjectMarksList();
            for (SubjectMarks subjectMarks: fill_subjectMarks){

                SubjectNameEnum subject = subjectMarks.getSubject();
                switch(subject) {
                    case SINHALA:
                        // code block
                        TIL_sinhala_Marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        Log.d(TAG, "openDialog: sinhalatil"+TIL_sinhala_Marks.getEditText().getText().toString());
                        break;
                    case ENGLISH:
                        TIL_maths_marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        break;
                    case MATHS:
                        TIL_english_marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        break;
                    case SCIENCE:
                        TIL_science_marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        break;
                    case TAMIL:
                        TIL_tamil_marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        break;
                    case BUDDHISM:
                        TIL_Buddhism_marks.getEditText().setText(String.valueOf(subjectMarks.getMarks()));
                        break;
                    default:
                        return;
                    // code block
                }

            }
        }




        builder.setView(view)
                .setTitle("View marks")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })

                .setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        dialog.show();
        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


    }

    private void setupRecycleView() {
       // SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String stdgrade = prf.getString("GRADE", null);



        Log.d(TAG, "setupRecycleView: grade"+stdgrade);

        if(stdgrade==null){
            throw new RuntimeException("grade should not be null");
        }

        getAllStudentMarksDetailsFromDB(stdgrade);

        Log.d(TAG, "setupRecycleView: grade"+stdgrade);

        Query query = collectionReference.whereEqualTo("grade", stdgrade)
                .orderBy("firstName", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<Parent> options = new FirestoreRecyclerOptions.Builder<Parent>()
                .setQuery(query, Parent.class).build();


        adapter = new StudentListAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {

                        Log.d(TAG, "setupRecycleView: true"+value.toString());



                    } else {
                        Log.d(TAG, "setupRecycleView: false"+value.toString());


                    }

                }


            }
        });

        adapter.setOnItemClickListner(new StudentListAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String parentId = documentSnapshot.getId();
                String name = documentSnapshot.getString("firstName");

                openDialog(name,parentId);



            }
        });


        recAddStudentMarks.setHasFixedSize(true);

        recAddStudentMarks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recAddStudentMarks.setAdapter(adapter);



    }


    private void getAllStudentMarksDetailsFromDB(String grade){
        if(grade==null || grade.isEmpty()){
            grade="3A";
        }
        db.collection("studentsmarks")
                .document(grade)
                .collection("Students")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Log.d(TAG, "onSuccess: db");
                        List<StudentMarks> studentMarks =  queryDocumentSnapshots.toObjects(StudentMarks.class);
                        store.StoreInitDB(studentMarks);

                        for(StudentMarks std:studentMarks){
                            Log.d(TAG, "onClick we: "+std.getName());
                        }

                        for(StudentMarks std:store.getAllStudentMarksDetails()){
                            Log.d(TAG, "onClick getAllStudentMarksDetails: "+std.getName());
                            StudentMarks fill_studentMarks = store.getSpecificStudentMarks(std.getStudentId());
                            Log.d(TAG, "onSuccess: "+fill_studentMarks.getStudentSubjectMarksList().toString());
                        }
                        // Log.d(TAG, studentMarks.);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,"fail"+ e.getLocalizedMessage() );
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }


}