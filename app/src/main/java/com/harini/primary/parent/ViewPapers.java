package com.harini.primary.parent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.harini.primary.R;
import com.harini.primary.adapters.ExamPaperAdapter;
import com.harini.primary.models.ExamPaper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewPapers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseStorage fstorage;

    private SweetAlertDialog pDialog;

    private static final int REQUEST_CODE=1;
    private static final int REQUESTCODE_STORAGE_PERMISSION = 99;

    private ExamPaperAdapter adapter;


    private RecyclerView recyleview_viewpapers;
    private LinearLayout hw_noresults_container;


    private String TAG="viewpapers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_papers);

        setupUi();
        init();
        setupRecycleView();

        Boolean readPermission = ActivityCompat.checkSelfPermission(ViewPapers.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writePermission = ActivityCompat.checkSelfPermission(ViewPapers.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!readPermission || !writePermission) {
            ActivityCompat.requestPermissions(ViewPapers.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUESTCODE_STORAGE_PERMISSION);

        }



    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fstorage = FirebaseStorage.getInstance();
        collectionReference = db.collection("Exams");
    }

    private void setupUi() {



        recyleview_viewpapers = findViewById(R.id.recyleview_viewpapers);
        hw_noresults_container = findViewById(R.id.hw_noresults_container);

    }

    private void setupRecycleView() {


         //FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", "null");

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException(" grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("stdclass", grade)
                .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ExamPaper> options = new FirestoreRecyclerOptions.Builder<ExamPaper>()
                .setQuery(query, ExamPaper.class).build();


        adapter = new ExamPaperAdapter(options,"student");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {


                        hw_noresults_container.setVisibility(View.GONE);
                        recyleview_viewpapers.setVisibility(View.VISIBLE);


                    } else {

                        recyleview_viewpapers.setVisibility(View.GONE);
                        hw_noresults_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


        recyleview_viewpapers.setHasFixedSize(true);

        recyleview_viewpapers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyleview_viewpapers.setAdapter(adapter);


        adapter.setOnItemClickListner(new ExamPaperAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                ExamPaper paper = documentSnapshot.toObject(ExamPaper.class);


                pDialog = new SweetAlertDialog(ViewPapers.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setCancelable(false);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("please wait...");
                pDialog.show();

                new Thread() {
                    public void run() {
                        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

                        Uri uri = Uri.parse(paper.getLink());

                        DownloadManager.Request request = new DownloadManager.Request(uri);


                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,paper.getFilename());

                        downloadManager.enqueue(request);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismissWithAnimation();
                                Toast toast = Toast.makeText(ViewPapers.this, "Downloaded", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }
                }.start();


               // Log.d(TAG, "onItemClick: link "+homework.getLink());

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


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // Log.d(TAG1, "onConfigurationChanged: ");
        //title.setText(getApplicationContext().getString(R.string.homeworkActivtiyTitle));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode==REQUESTCODE_STORAGE_PERMISSION){



            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {


            }
            else{

                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is need to give a better user experience  ")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ViewPapers.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create().show();
            }



        }



    }
}