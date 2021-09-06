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
import com.harini.primary.models.TeacherChatQueue;
import com.harini.primary.adapters.TeacherChatQueueAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Chat extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SweetAlertDialog pDialog;
    private TeacherChatQueueAdapter adapter;

    private RecyclerView rec_teacher_chat_queue;
    private CollectionReference collectionReference;
    private FloatingActionButton btnselectuser;

    private String TAG="teacherchat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setupUi();
        init();
        setupRecycleView();

        btnselectuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(getApplicationContext(), SelectParentChat.class);
                startActivity(newIntent);

            }
        });
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("messages");
    }

    private void setupUi() {

        rec_teacher_chat_queue = findViewById(R.id.rec_teacher_chat_queue);
        btnselectuser = findViewById(R.id.btnselectuser);

    }

    private void setupRecycleView() {
        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        if(grade==null){
            throw new RuntimeException(" grade should not be null");
        }


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference.whereEqualTo("grade",grade)
                .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<TeacherChatQueue> options = new FirestoreRecyclerOptions.Builder<TeacherChatQueue>()
                .setQuery(query, TeacherChatQueue.class).build();


        adapter = new TeacherChatQueueAdapter(options);

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

        adapter.setOnItemClickListner(new TeacherChatQueueAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String conversationId = documentSnapshot.getId();
                Log.d(TAG, "onItemClick: "+conversationId);
               // Toast.makeText(getApplicationContext(),"convo"+conversationId,Toast.LENGTH_LONG).show();


                Intent newIntent = new Intent(getApplicationContext(), SingleChat.class);
                newIntent.putExtra("EXTRA_CONVO_ID", conversationId);
                startActivity(newIntent);


            }
        });

        rec_teacher_chat_queue.setHasFixedSize(true);

        rec_teacher_chat_queue.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rec_teacher_chat_queue.setAdapter(adapter);



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
