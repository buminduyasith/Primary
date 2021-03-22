package com.harini.primary.parent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.harini.primary.Models.Homework;
import com.harini.primary.Models.Teacher;
import com.harini.primary.R;
import com.harini.primary.adapters.HomeWorkAdapter;
import com.harini.primary.teacher.AdHomeWork;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewHomeWork extends AppCompatActivity {

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

    private static final int REQUEST_CODE=1;


    private String TAG="viewhomework";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_home_work);

        setupUi();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fstorage = FirebaseStorage.getInstance();

        collectionReference = db.collection("HomeWorks");

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission error","You have permission");


        }
        else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }


        setupRecycleView();
    }

    private void setupUi() {


        recyleview_homeworks = findViewById(R.id.recyleview_homeworks);
        hw_noresults_container = findViewById(R.id.hw_noresults_container);
    }

    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException("p grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("stdclass", grade).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Homework> options = new FirestoreRecyclerOptions.Builder<Homework>()
                .setQuery(query, Homework.class).build();


        adapter = new HomeWorkAdapter(options,"PARENT");

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

                Homework homework = documentSnapshot.toObject(Homework.class);


                pDialog = new SweetAlertDialog(ViewHomeWork.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setCancelable(false);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("please wait...");
                pDialog.show();

                new Thread() {
                    public void run() {
                        DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

                        Uri uri = Uri.parse(homework.getLink());

                        DownloadManager.Request request = new DownloadManager.Request(uri);


                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,homework.getDiscription());

                        downloadManager.enqueue(request);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismissWithAnimation();
                                Toast toast = Toast.makeText(ViewHomeWork.this, "Downloaded", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }
                }.start();


                Log.d(TAG, "onItemClick: link "+homework.getLink());

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                //authandredirectuser();
            }
            else{

                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is need to give a better user experience  ")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ViewHomeWork.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

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