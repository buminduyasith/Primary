package com.harini.primary.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.harini.primary.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddExamPapers extends AppCompatActivity implements View.OnClickListener {


    private ImageButton imgbtn_upload;
    private final static int FILE_REQUEST_CODE = 1;

    private FirebaseStorage fstorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String TAG="addhomework";

    private SweetAlertDialog pDialog;

    private Spinner spinner_category;

    private Button btnUpload;

    private TextInputLayout txtinput_fileName;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam_papers);

        setupUI();
        setActions();
        init();
    }


    private void setupUI(){
         imgbtn_upload = findViewById(R.id.imgbtn_upload);
        spinner_category = findViewById(R.id.spinner_category);
        txtinput_fileName = findViewById(R.id.txtinput_fileName);
        btnUpload = findViewById(R.id.btnUpload);


    }

    private void setActions(){
        imgbtn_upload.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

//        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                Toast.makeText(getApplicationContext(),spinner_category.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fstorage = FirebaseStorage.getInstance();
        collectionReference = db.collection("Exams");

        List<String> subjects = new ArrayList<>();

        subjects.add("sinhala");
        subjects.add("mathematics");
        subjects.add("english");
        subjects.add("science");

        ArrayAdapter<String> gradeAdapteer = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                subjects

        );

        spinner_category.setAdapter(gradeAdapteer);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

           case R.id.imgbtn_upload:
               openfile();
               break;
            case R.id.btnUpload:
                String filename = txtinput_fileName.getEditText().getText().toString();
                String subject = spinner_category.getSelectedItem().toString();

                if(filename.isEmpty() || subject.isEmpty() || uri ==null ){
                    Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                }else{
                    uploadfile(uri);
                }


                break;

            default:
                return;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == FILE_REQUEST_CODE) {

                 uri = data.getData();

                //uploadfile(uri);
                Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();

            }


        } else {
            // Log.d(TAG, "onActivityResult: canceled");
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }

    }

        private void openfile(){
        Intent galleyIntent = new Intent();
        galleyIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("application/pdf");

        startActivityForResult(galleyIntent, FILE_REQUEST_CODE);
    }

    private void uploadfile(Uri uri){


        pDialog = new SweetAlertDialog(AddExamPapers.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("please wait...");
        pDialog.show();

        StorageReference storageReference = fstorage.getReference();

        StorageReference dpref = storageReference.child("files");
        //StorageReference ref = dpref.child("images/newimg25");


        Timestamp timestamp = new Timestamp(new Date());

        //todo add file name
        String dt = new Date().getTime()+"";
        String filename = txtinput_fileName.getEditText().getText().toString()+dt;
        //add timestampe to file path



        //String selectedSubject = spinner_category.getSelectedItem().toString();

        StorageReference riversRef = dpref.child("papers/"+filename);


        UploadTask uploadTask = riversRef.putFile(uri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pDialog.dismiss();
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d(TAG, "onSuccess: "+taskSnapshot.getMetadata().getPath());

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                Log.d(TAG, "Upload paused"+snapshot.getBytesTransferred()+"upload and total"+snapshot.getTotalByteCount());

            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    pDialog.dismiss();
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                Log.d(TAG, "then: url task about to return downloadurl");
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();


                    Log.d(TAG, "onComplete: link harini"+downloadUri);
                    Log.d(TAG, "onComplete: link harini"+downloadUri.toString());

                    insertdatatodb(downloadUri.toString());
                   // pDialog.dismiss();
                    Log.d(TAG, "onComplete: link "+downloadUri);
                } else {
                    // Handle failures
                    pDialog.dismiss();
                    Log.d(TAG, "onComplete: link"+task.getException().getMessage());
                    // ...
                }
            }
        });



    }

    private void insertdatatodb(String linkfile){


        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException("t grade should not be null");
        }

        Log.d(TAG, "insertdatatodb: string uri"+linkfile);
        //Log.d(TAG, "insertdatatodb: "+link.toString());
        Timestamp timestamp = new Timestamp(new Date());


        String filename = txtinput_fileName.getEditText().getText().toString();
        String subject = spinner_category.getSelectedItem().toString();

        Map<String, Object> hw = new HashMap<>();
        hw.put("filename", filename);
        hw.put("subject", subject);
        hw.put("link", linkfile);
        hw.put("timestamp", timestamp);
        hw.put("stdclass", grade);

        db.collection("Exams").add(hw)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: data added to firestore ");
                        pDialog.dismissWithAnimation();

                        pDialog = new SweetAlertDialog(AddExamPapers.this, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setCancelable(false);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Exam papers");
                        pDialog.setContentText(" Exam paper added successfully..");
                        pDialog.setConfirmButton("Done\n", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.dismissWithAnimation();

                            }
                        });

                        pDialog.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: ");
                pDialog.dismissWithAnimation();
                pDialog = new SweetAlertDialog(AddExamPapers.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText("Create Homeworks");
                pDialog.setContentText(e.getLocalizedMessage());
                pDialog.show();
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }


}