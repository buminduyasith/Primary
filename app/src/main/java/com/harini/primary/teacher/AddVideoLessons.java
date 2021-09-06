package com.harini.primary.teacher;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.harini.primary.R;
import com.harini.primary.adapters.AddVideoLessonsAdapter;
import com.harini.primary.models.VideoLesson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddVideoLessons extends AppCompatActivity  {

    private FloatingActionButton Fab_addvideo;

    private RecyclerView recyleview_videolessons;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private AddVideoLessonsAdapter adapter;

    private LinearLayout videolessons_noresults_container;

    private SweetAlertDialog pDialog;


    private static final String TAG = "videolessondbug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_lessons);

        setupUi();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("VideoLessons");


        setupRecycleView();


        Fab_addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


    }


    private void setupUi() {

        Fab_addvideo = findViewById(R.id.Fab_addvideo);
        recyleview_videolessons = findViewById(R.id.recyleview_videolessons);
        videolessons_noresults_container = findViewById(R.id.videolessons_noresults_container);
    }


    private void setupRecycleView() {

        // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        Log.d(TAG, "setupRecycleView: grade"+grade);

        if(grade==null){
            throw new RuntimeException("grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("class", grade).orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<VideoLesson> options = new FirestoreRecyclerOptions.Builder<VideoLesson>()
                .setQuery(query, VideoLesson.class).build();


        adapter = new AddVideoLessonsAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {


                        videolessons_noresults_container.setVisibility(View.GONE);
                        recyleview_videolessons.setVisibility(View.VISIBLE);


                    } else {

                        recyleview_videolessons.setVisibility(View.GONE);
                        videolessons_noresults_container.setVisibility(View.VISIBLE);

                    }

                }


            }
        });


        recyleview_videolessons.setHasFixedSize(true);

        recyleview_videolessons.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyleview_videolessons.setAdapter(adapter);


        adapter.setOnItemClickListner(new AddVideoLessonsAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                adapter.deleteItem(position);

            }
        });


    }


    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddVideoLessons.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add_videolessons, null);

        final TextInputLayout title = view.findViewById(R.id.TIL_video_title);
        final TextInputLayout urlcode = view.findViewById(R.id.TIL_url_code);


        builder.setView(view)
                .setTitle("Video Lessons")
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final DialogInterface d = dialog;

                        Log.d(TAG, "onClick: postive click");
                        String yturlcode = urlcode.getEditText().getText().toString();
                        String yttitle = title.getEditText().getText().toString();

                        if (yttitle.isEmpty() || yturlcode.isEmpty()) {
                            Log.d(TAG, "onClick: feilds empty");
                            Toast.makeText(AddVideoLessons.this, "required_field_missing", Toast.LENGTH_SHORT);
                            return;
                        }


                        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

                        String grade = prf.getString("GRADE", null);

                        if (grade == null) {
                            Log.d(TAG, "onClick: grade null");
                            Toast.makeText(AddVideoLessons.this, "grade null", Toast.LENGTH_SHORT);
                            return;
                        }

                        Timestamp timestamp = new Timestamp(new Date());



                        Map<String, Object> video = new HashMap<>();
                        video.put("title", yttitle);
                        video.put("videoid", yturlcode);
                        video.put("timestamp", timestamp);
                        video.put("class", grade);

                        db.collection("VideoLessons").add(video)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "onSuccess: ");
                                        pDialog = new SweetAlertDialog(AddVideoLessons.this, SweetAlertDialog.SUCCESS_TYPE);
                                        pDialog.setCancelable(false);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText("Video Lessons");
                                        pDialog.setContentText(" Video Lesson added successfully..");
                                        pDialog.setConfirmButton("Done\n", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();

                                            }
                                        });
                                        d.dismiss();
                                        pDialog.show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: ");
                                pDialog = new SweetAlertDialog(AddVideoLessons.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog.setCancelable(true);
                                pDialog.setTitleText("Video Lessons");
                                pDialog.setContentText(e.getLocalizedMessage());
                                d.dismiss();
                                pDialog.show();
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });


                    }
                })

                .setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        dialog.show();
        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


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