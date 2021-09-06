package com.harini.primary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.Models.Parent;
import com.harini.primary.Models.TeacherChatQueue;
import com.harini.primary.adapters.StudentListAdapter;
import com.harini.primary.adapters.TeacherChatQueueAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectParentChat extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private StudentListAdapter adapter;

    private RecyclerView rec_select_parent;
    private CollectionReference collectionReference;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parent_chat);

        setupUi();
        init();
        setupRecycleView();


    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Parents");
    }

    private void setupUi() {

        rec_select_parent = findViewById(R.id.rec_select_parent);


    }

    private void setupRecycleView() {
        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        if(grade==null){
            throw new RuntimeException(" grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("grade", grade)
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
                Log.d(TAG, "onItemClick: "+parentId);
                //Toast.makeText(getApplicationContext(),"convo"+parentId,Toast.LENGTH_LONG).show();

               // Log.d(TAG, "onItemClick: name user");
                Intent newIntent = new Intent(getApplicationContext(), SingleChat.class);
                newIntent.putExtra("EXTRA_CONVO_ID", parentId);
                newIntent.putExtra("EXTRA_NAME",name );
                startActivity(newIntent);

            }
        });


        rec_select_parent.setHasFixedSize(true);

        rec_select_parent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rec_select_parent.setAdapter(adapter);



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