package com.harini.primary.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.harini.primary.R;
import com.harini.primary.SingleChat;
//import com.harini.primary.adapters.StudentAddMarkAdapter;
import com.harini.primary.Store;
import com.harini.primary.adapters.StudentListAdapter;
import com.harini.primary.adapters.SubjectMarks;
import com.harini.primary.models.ExamDetails;
import com.harini.primary.models.ExamSummaryClass;
import com.harini.primary.models.Parent;
import com.harini.primary.models.StudentMarks;
import com.harini.primary.models.SubjectNameEnum;
import com.harini.primary.teacher.AddVideoLessons;
import com.harini.primary.utill.HintSpinnerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.harini.primary.models.SubjectNameEnum.*;

public class AddStudentMarks extends AppCompatActivity {

    private static final String TAG ="addstdM" ;
    private RecyclerView recAddStudentMarks;
   // private StudentAddMarkAdapter adapter;
    private static List<ExamDetails> examDetailsList;
    private Button btn_add_mark,btn_search;
    private  static int stepCount = 0;
    private TextView txtsubjectName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner spinner_grade,spinner_term;
    private SweetAlertDialog pDialog;

    private ProgressBar progressBar;
    private CollectionReference collectionReference;
    private List<String> gradelist;
    private List<String> terms;
    private StudentListAdapter adapter;
   private  Store store;
   private  int count;


    //    private static  List<ExamDetails> examDetailsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_marks);

        setupUi();
        init();
        setupRecycleView("");
        loadClasses();
       // getSchools();
       getAllStudentMarksDetailsFromDB(null,null);

        store  = Store.getInstance();






        btn_add_mark.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                pDialog = new SweetAlertDialog(AddStudentMarks.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setCancelable(false);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("please wait ...");
                            //pDialog.setContentText("All marks added successfully..");
                pDialog.show();

                List<StudentMarks> studentMarksListtemp = store.getAllStudentMarksDetails(spinner_term.getSelectedItem().toString());
//
//               for(StudentMarks std:studentMarksListtemp){
//                   Log.d(TAG, "onClick: "+std.getName() + " size"+std.getStudentSubjectMarksList().size());
//                }

                //    store.summaryGenerate();
                if(studentMarksListtemp.size()>20){
                    throw  new RuntimeException("studentMarksListtemp overflow");
                }
                 count = 1;
                for(StudentMarks std:studentMarksListtemp){
                    WriteBatch batch = db.batch();
                    String cusDocId = std.getStudentId() + "" + spinner_term.getSelectedItem().toString();
                    Log.d(TAG, "onClick: adding marks to db"+std.toString());
                    DocumentReference nycRef = db.collection("studentsmarks")
                            .document(spinner_grade.getSelectedItem().toString())
                            .collection("Students").document(cusDocId);
                    batch.set(nycRef,std);
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Log.d(TAG, "onComplete: size"+studentMarksListtemp.size()+ " count"+ count);
                            if(count ==studentMarksListtemp.size()){
                                Log.d(TAG, "onComplete: "+count);
                                pDialog.dismissWithAnimation();
                                pDialog = new SweetAlertDialog(AddStudentMarks.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Exam marks");
                                pDialog.setContentText("All marks added successfully..");
                                pDialog.show();
                                count=0;
                            }
                            count++;

                        }
                    });



                }
                //pDialog.dismissWithAnimation();

              //  addSummaryToDB();

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grade = spinner_grade.getSelectedItem().toString();
                String term = spinner_term.getSelectedItem().toString();

               if(grade==null || grade.isEmpty()){
                   throw new  NullPointerException("grade can't be a null value");

               }

               else if(term==null || term.isEmpty()){
                   throw new  NullPointerException("grade can't be a null value");
               }

                updateQuery(grade);
               //empty the store static array
                getAllStudentMarksDetailsFromDB(grade,term);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addSummaryToDB(){


        pDialog = new SweetAlertDialog(AddStudentMarks.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("please wait  ...");
        //pDialog.setContentText("All marks added successfully..");
        pDialog.show();


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request
                ExamSummaryClass examSummaryClass=  store.summaryGenerate();
                WriteBatch batch = db.batch();

                examSummaryClass.setTerm(spinner_term.getSelectedItem().toString());
                examSummaryClass.setGrade(spinner_grade.getSelectedItem().toString());

                DocumentReference nycRef = db.collection("summary").document();
//                .document(spinner_grade.getSelectedItem().toString())
//                .collection("Students").document();
                batch.set(nycRef,examSummaryClass);
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        AddStudentMarks.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                   // pDialog.dismiss();
                                    pDialog.dismissWithAnimation();
                                }catch (Exception e){
                                    pDialog.dismissWithAnimation();
                                }
                            }//public void run() {
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        pDialog.dismissWithAnimation();
                        pDialog = new SweetAlertDialog(AddStudentMarks.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.setCancelable(false);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("exam summary ");
                        pDialog.setContentText(e.getLocalizedMessage());
                        pDialog.show();
                    }
                });
            }
        });
        thread.start();




    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Parents");


    }

    private void setupUi() {

        recAddStudentMarks = findViewById(R.id.recAddStudentMarks);
        btn_add_mark = findViewById(R.id.btn_add_mark);
        txtsubjectName = findViewById(R.id.txtsubjectName);
        spinner_grade = findViewById(R.id.spinner_grade);
        spinner_term = findViewById(R.id.spinner_term);
        btn_search = findViewById(R.id.btn_search);

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


//        spinner_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                String grade = gradelist.get(position);
//                String term;
//                if( spinner_term.getSelectedItem()!=null){
//
//                    term = spinner_term.getSelectedItem().toString();
//                }
//                else{
//                  //  grade = "3A";
//                    term ="1st";
//                }
//
//                updateQuery(grade);
//                getAllStudentMarksDetailsFromDB(grade,term);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//
//        spinner_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                String grade;
//                String term;
//                if( spinner_grade.getSelectedItem()!=null){
//                     grade = spinner_grade.getSelectedItem().toString();
//                     term = spinner_term.getSelectedItem().toString();
//                }
//                else{
//                    grade = "3A";
//                    term ="1st";
//                }
//
//
//                Log.d(TAG, "onItemSelected: grade"+grade+" term"+term);
////
////                updateQuery(grade);
////                getAllStudentMarksDetailsFromDB(grade);
//
//
//
////                Query query = collectionReference.whereEqualTo("grade", grade)
////                        .orderBy("firstName", Query.Direction.ASCENDING);
////
//////                Query query = collectionReference.whereEqualTo("grade", grade)
//////                        .orderBy("firstName", Query.Direction.ASCENDING);
////
////
////                FirestoreRecyclerOptions<Parent> options = new FirestoreRecyclerOptions.Builder<Parent>()
////                        .setQuery(query, Parent.class).build();
////
////
////                //   adapter = new StudentListAdapter(options);
////                adapter.updateOptions(options);
//
//                getAllStudentMarksDetailsFromDB(grade,term);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });


    }

    private void updateQuery(String grade){
        Query query = collectionReference.whereEqualTo("grade", grade)
                .orderBy("firstName", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<Parent> options = new FirestoreRecyclerOptions.Builder<Parent>()
                .setQuery(query, Parent.class).build();


        //   adapter = new StudentListAdapter(options);
        adapter.updateOptions(options);


    }

    private void loadClasses() {
        //selectedClass = null;
        ArrayList<String> listClasses = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.grade_array)));
        listClasses.add(getString(R.string.select_class));
        HintSpinnerAdapter classesAdapter = new HintSpinnerAdapter(this, R.layout.spinner_item, listClasses);
        spinner_grade.setAdapter(classesAdapter);
        spinner_grade.setSelection(classesAdapter.getCount());
    }

    private void getSchools() {


       // progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();


         gradelist = new ArrayList<>();

        ArrayAdapter<String> gradeAdapteer = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                gradelist

        );

        spinner_grade.setAdapter(gradeAdapteer);


        db.collection("teachers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot snapshot : task.getResult()) {


                               // progressBar.setVisibility(View.GONE);

                                gradelist.add(snapshot.getString("grade"));



                            }


                            gradeAdapteer.notifyDataSetChanged();
                            updateQuery(gradelist.get(0));



                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

               // progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private int filtermarks(String marks){

        if(marks.isEmpty()|| marks==null||marks==""){
            return 0;
        }
        else{
            return Integer.valueOf(marks);
        }
    }

    private void openDialog(String name,String studentId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentMarks.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_marks, null);


        final TextInputLayout TIL_Buddhism_marks = view.findViewById(R.id.TIL_Buddhism_marks);
        final TextInputLayout TIL_sinhala_Marks = view.findViewById(R.id.TIL_sinhala_Marks);
        final TextInputLayout TIL_maths_marks = view.findViewById(R.id.TIL_maths_marks);
        final TextInputLayout TIL_english_marks = view.findViewById(R.id.TIL_english_marks);
        final TextInputLayout TIL_science_marks = view.findViewById(R.id.TIL_science_marks);
        final TextInputLayout TIL_tamil_marks = view.findViewById(R.id.TIL_tamil_marks);
        final TextView txtStudentName = view.findViewById(R.id.txtStudentName);
        txtStudentName.setText(name);

        Log.d(TAG, "fill_studentMarks "+spinner_term.getSelectedItem().toString());
        StudentMarks fill_studentMarks = store.getSpecificStudentMarks(studentId,spinner_term.getSelectedItem().toString());


        if(fill_studentMarks!=null){
            List<SubjectMarks> fill_subjectMarks = fill_studentMarks.getStudentSubjectMarksList();
            for (SubjectMarks subjectMarks: fill_subjectMarks){

                SubjectNameEnum subject = subjectMarks.getSubject();
                switch(subject) {
                    case SINHALA:
                        // code block
                        TIL_sinhala_Marks.getEditText().setText(marksformat(subjectMarks.getMarks()));
                        Log.d(TAG, "openDialog: sinhalatil"+TIL_sinhala_Marks.getEditText().getText().toString());
                        break;
                    case ENGLISH:
                        TIL_english_marks.getEditText().setText(marksformat(subjectMarks.getMarks()));

                        break;
                    case MATHS:
                        TIL_maths_marks.getEditText().setText(marksformat(subjectMarks.getMarks()));
                        break;
                    case SCIENCE:
                        TIL_science_marks.getEditText().setText(marksformat(subjectMarks.getMarks()));
                        break;
                    case TAMIL:
                        TIL_tamil_marks.getEditText().setText(marksformat(subjectMarks.getMarks()));
                        break;
                    case BUDDHISM:
                        TIL_Buddhism_marks.getEditText().setText(marksformat(subjectMarks.getMarks()));
                        break;
                    default:
                        return;
                        // code block
                }

            }
        }




        builder.setView(view)
                .setTitle("Add Marks")
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final DialogInterface d = dialog;

                        Log.d(TAG, "onClick: postive click");
                        String sinhalaM = TIL_sinhala_Marks.getEditText().getText().toString();
                        String mathsM = TIL_maths_marks.getEditText().getText().toString();
                        String englishM = TIL_english_marks.getEditText().getText().toString();
                        String scienceM = TIL_science_marks.getEditText().getText().toString();
                        String tamilM = TIL_tamil_marks.getEditText().getText().toString();
                        String buddhism = TIL_Buddhism_marks.getEditText().getText().toString();

                        List<SubjectMarks> subjectMarksList = new ArrayList<>();
                        subjectMarksList.add(new SubjectMarks(SINHALA,filtermarks(sinhalaM)));
                        subjectMarksList.add(new SubjectMarks(MATHS,filtermarks(mathsM)));
                        subjectMarksList.add(new SubjectMarks(ENGLISH,filtermarks(englishM)));
                        subjectMarksList.add(new SubjectMarks(SCIENCE,filtermarks(scienceM)));
                        subjectMarksList.add(new SubjectMarks(TAMIL,filtermarks(tamilM)));
                        subjectMarksList.add(new SubjectMarks(BUDDHISM,filtermarks(buddhism)));

                        String term = spinner_term.getSelectedItem().toString();
                        store.addStudentMark(new StudentMarks(studentId,subjectMarksList,name,term));

                        double totalMarks = filtermarks(sinhalaM)+
                                filtermarks(mathsM)+
                                filtermarks(englishM)+filtermarks(scienceM)+filtermarks(tamilM);




//                        if (yttitle.isEmpty() || yturlcode.isEmpty()) {
//                            Log.d(TAG, "onClick: feilds empty");
//                            Toast.makeText(AddStudentMarks.this, "required_field_missing", Toast.LENGTH_SHORT);
//                            return;
//                        }


//                        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);
//
//                        String grade = prf.getString("GRADE", null);

//                        if (grade == null) {
//                            Log.d(TAG, "onClick: grade null");
//                            Toast.makeText(AddStudentMarks.this, "grade null", Toast.LENGTH_SHORT);
//                            return;
//                        }

                        Timestamp timestamp = new Timestamp(new Date());

                        Log.d(TAG, "onClick: "+sinhalaM+" "+mathsM+" "+englishM);






                    }
                })

                .setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        dialog.show();
        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


    }

    private void setupRecycleView(String stdgrade) {
        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);



        Log.d(TAG, "setupRecycleView: grade"+stdgrade);

        String term = spinner_term.getSelectedItem().toString();

        Query query = collectionReference.whereEqualTo("grade", stdgrade)
                .whereEqualTo("term",term)
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
//                String name = documentSnapshot.getString("firstName");
                String name = documentSnapshot.getString("studentName");

                openDialog(name,parentId);



            }
        });


        recAddStudentMarks.setHasFixedSize(true);

        recAddStudentMarks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recAddStudentMarks.setAdapter(adapter);



    }


    private void getAllStudentMarksDetailsFromDB(String grade,String term){

        pDialog = new SweetAlertDialog(AddStudentMarks.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("please wait  ...");
        //pDialog.setContentText("All marks added successfully..");
        pDialog.show();
        if(grade==null || grade.isEmpty()){
            grade="3A";
        }

        else if(term==null || term.isEmpty()){
            term = "1st";
        }

        db.collection("studentsmarks")
                .document(grade)
                .collection("Students")
                .whereEqualTo("term",term)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Log.d(TAG, "onSuccess: db");
                        List<StudentMarks> studentMarks =  queryDocumentSnapshots.toObjects(StudentMarks.class);
                        store.StoreInitDB(studentMarks);
                        pDialog.dismissWithAnimation();

//               for(StudentMarks std:studentMarks){
//                   Log.d(TAG, "onClick we: "+std.getName());
//                }
//
//                        for(StudentMarks std:store.getAllStudentMarksDetails()){
//                            Log.d(TAG, "onClick getAllStudentMarksDetails: "+std.getName());
//                            StudentMarks fill_studentMarks = store.getSpecificStudentMarks(std.getStudentId());
//                            Log.d(TAG, "onSuccess: "+fill_studentMarks.getStudentSubjectMarksList().toString());
//                        }
                       // Log.d(TAG, studentMarks.);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG,"fail"+ e.getLocalizedMessage() );
                pDialog.dismissWithAnimation();
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