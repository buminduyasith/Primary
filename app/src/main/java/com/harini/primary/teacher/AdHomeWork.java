package com.harini.primary.teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.harini.primary.Models.Homework;
import com.harini.primary.Models.VideoLesson;
import com.harini.primary.R;
import com.harini.primary.adapters.AddVideoLessonsAdapter;
import com.harini.primary.adapters.HomeWorkAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdHomeWork extends AppCompatActivity {

    private FloatingActionButton Fab_addhw;

    private RecyclerView recyleview_homeworks;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private HomeWorkAdapter adapter;

    private LinearLayout hw_noresults_container;

    private SweetAlertDialog pDialog;
    private String TAG="addhomework";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_home_work);

        setupUi();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("VideoLessons");


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

        Query query = collectionReference.whereEqualTo("class", grade).orderBy("timestamp", Query.Direction.DESCENDING);


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


//        adapter.setOnItemClickListner(new AddVideoLessonsAdapter.onItemClickListner() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//
//                adapter.deleteItem(position);
//
//            }
//        });


    }


    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdHomeWork.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add_homework, null);

        final Button btn_select_file = view.findViewById(R.id.btn_select_file);
        final TextInputLayout TIL_hw_discription = view.findViewById(R.id.TIL_hw_discription);


        builder.setView(view)
                .setTitle("Video Lessons")
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String discription = TIL_hw_discription.getEditText().getText().toString();

                        final DialogInterface d = dialog;



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