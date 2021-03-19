package com.harini.primary.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.Models.Announcement;
import com.harini.primary.Models.VideoLesson;
import com.harini.primary.R;
import com.harini.primary.adapters.AddVideoLessonsAdapter;
import com.harini.primary.adapters.AnnouncementRecycleViewAdapter;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddVideoLessons extends AppCompatActivity {

    private FloatingActionButton Fab_addvideo;

    private RecyclerView recyleview_videolessons;




    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private AddVideoLessonsAdapter adapter;

    private LinearLayout videolessons_noresults_container;


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
                //openDialog();
            }
        });




    }


    private void setupUi(){

        Fab_addvideo = findViewById(R.id.Fab_addvideo);
        recyleview_videolessons = findViewById(R.id.recyleview_videolessons);
        videolessons_noresults_container = findViewById(R.id.videolessons_noresults_container);
    }


    private void setupRecycleView(){

       // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        Query query = collectionReference.whereEqualTo("class","5A").orderBy("timestamp",Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<VideoLesson> options = new FirestoreRecyclerOptions.Builder<VideoLesson>()
                .setQuery(query,VideoLesson.class).build();




        adapter = new AddVideoLessonsAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {




                if(value !=null){



                    if(value.size()>0){


                        videolessons_noresults_container.setVisibility(View.GONE);
                        recyleview_videolessons.setVisibility(View.VISIBLE);


                    }
                    else{

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