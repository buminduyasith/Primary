package com.harini.primary.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.harini.primary.R;
import com.harini.primary.adapters.HomeWorkAdapter;
import com.harini.primary.models.Homework;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdHomeWork extends AppCompatActivity {

    private FloatingActionButton Fab_addhw;

    private FirebaseStorage fstorage;

    private final static int FILE_REQUEST_CODE = 3;

    private RecyclerView recyleview_homeworks;



    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private HomeWorkAdapter adapter;

    private LinearLayout hw_noresults_container;

    private SweetAlertDialog pDialog;

    private DialogInterface dialogInterface ;

    private String discription;


    private String TAG="addhomework";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_home_work);

        setupUi();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fstorage = FirebaseStorage.getInstance();

        collectionReference = db.collection("HomeWorks");


        setupRecycleView();


        Fab_addhw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }


    private void setupUi() {

        Fab_addhw = findViewById(R.id.Fab_addhw);
        recyleview_homeworks = findViewById(R.id.recyleview_homeworks);
        hw_noresults_container = findViewById(R.id.hw_noresults_container);
    }


    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException("t grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("stdclass", grade).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Homework> options = new FirestoreRecyclerOptions.Builder<Homework>()
                .setQuery(query, Homework.class).build();


        adapter = new HomeWorkAdapter(options,"TEACHER");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {


                        hw_noresults_container.setVisibility(View.GONE);
                        recyleview_homeworks.setVisibility(View.VISIBLE);


                    } else {

                        recyleview_homeworks.setVisibility(View.GONE);
                        hw_noresults_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


        recyleview_homeworks.setHasFixedSize(true);

        recyleview_homeworks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyleview_homeworks.setAdapter(adapter);


       adapter.setOnItemClickListner(new HomeWorkAdapter.onItemClickListner() {
           @Override
           public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
               adapter.deleteItem(position);
           }
       });


    }


    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdHomeWork.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add_homework, null);

        final Button btn_select_file = view.findViewById(R.id.btn_select_file);
        final TextInputLayout TIL_hw_discription = view.findViewById(R.id.TIL_hw_discription);
        final ProgressBar pb_hw_uploading = view.findViewById(R.id.pb_hw_uploading);


        builder.setView(view)
                .setTitle("Video Lessons")
//                .setPositiveButton("done", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialogInterface = dialog;
//
//                        String discription = TIL_hw_discription.getEditText().getText().toString();
//
//                        if(discription.isEmpty()){
//                            Toast.makeText(AdHomeWork.this,"please add a discription",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        dialogInterface.dismiss();
//                       // pb_hw_uploading.setVisibility(View.VISIBLE);
//                       // openfile();
//
//
//                        //dialog.dismiss();
//
//                        //final DialogInterface d = dialog;
//
//
//
//
//
//                    }
//                })

                .setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        btn_select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TIL_hw_discription.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(AdHomeWork.this,"please add a discription",Toast.LENGTH_SHORT).show();
                    return;
                }
                //pb_hw_uploading.setVisibility(View.VISIBLE);
                discription = TIL_hw_discription.getEditText().getText().toString();
                dialog.dismiss();
                openfile();

            }
        });

        dialog.show();
        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


    }

    private void openfile(){
        Intent galleyIntent = new Intent();
        galleyIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("application/pdf");

        startActivityForResult(galleyIntent, FILE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            
            if(requestCode==FILE_REQUEST_CODE){

                Uri uri = data.getData();

                uploadfile(uri);

                Log.d(TAG, "onActivityResult: uri"+uri);
            }
            
        }
        else{
            Log.d(TAG, "onActivityResult: canceled");
        }

    }

    private void uploadfile(Uri uri){


        pDialog = new SweetAlertDialog(AdHomeWork.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("please wait...");
        pDialog.show();

        StorageReference storageReference = fstorage.getReference();

        StorageReference dpref = storageReference.child("files");
        //StorageReference ref = dpref.child("images/newimg25");


        Timestamp timestamp = new Timestamp(new Date());

        String dt = new Date().getTime()+"";
        //add timestampe to file path
        StorageReference riversRef = dpref.child("homework/"+discription+dt);


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
                    //pDialog.dismiss();
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


        Map<String, Object> hw = new HashMap<>();
        hw.put("discription", discription);
        hw.put("link", linkfile);
        hw.put("timestamp", timestamp);
        hw.put("stdclass", grade);

        db.collection("HomeWorks").add(hw)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: data added to firestore ");
                        pDialog.dismissWithAnimation();

                        pDialog = new SweetAlertDialog(AdHomeWork.this, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setCancelable(false);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Create HomeWork");
                        pDialog.setContentText(" Homework added successfully..");
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
                pDialog = new SweetAlertDialog(AdHomeWork.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText("Create Homeworks");
                pDialog.setContentText(e.getLocalizedMessage());
                pDialog.show();
                Log.d(TAG, "onFailure: " + e.getMessage());
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