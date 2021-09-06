package com.harini.primary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.models.Message;
import com.harini.primary.adapters.MessageAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SingleChat extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SweetAlertDialog pDialog;
    private MessageAdapter adapter;

    private RecyclerView rec_chat;
    private CollectionReference collectionReference;
    private String name;

    private String TAG="teacherchat";
    private ImageButton btnsend;
    private TextInputLayout txtinput_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        setupUi();
        init();
        setupRecycleView();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    private void setupUi() {

        rec_chat = findViewById(R.id.rec_chat);
        btnsend = findViewById(R.id.btnsend);
        txtinput_msg = findViewById(R.id.txtinput_msg);

    }

    private void sendMsg(){

        String msg = txtinput_msg.getEditText().getText().toString();
        if(msg==null || msg.isEmpty()){
            return;
        }

        String convoId = getIntent().getStringExtra("EXTRA_CONVO_ID");
         name = getIntent().getStringExtra("EXTRA_NAME");

//        if(name==null){
//            DocumentReference docRef = db.collection("Parents").document(convoId);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            name = document.getString("firstName");
//                            Log.d(TAG, "onComplete: "+name);
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
//        }

        //String msg = txtinput_msg.getEditText().getText().toString();
        Timestamp timestamp = new Timestamp(new Date());
        String senderRole = "";
        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);
        String grade = prf.getString("GRADE", null);
        if(grade!=null){
            senderRole = "TEACHER";
        }else{
            senderRole = "STUDENT";
        }



        Map<String, Object> recent = new HashMap<>();
        recent.put("grade", grade);
        recent.put("name", name);
        recent.put("recentMsg", msg);
        recent.put("teacherid", mAuth.getCurrentUser().getUid());
        recent.put("studentId",convoId);
        recent.put("timestamp", timestamp);

        Map<String, Object> hw = new HashMap<>();
        hw.put("msg", msg);
        hw.put("senderRole", senderRole);
        hw.put("timestamp", timestamp);
       // hw.put("timestamp", timestamp);



        db.collection("messages")
                .document(convoId)
                .set(recent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("messages")
                        .document(convoId)
                        .collection("msg")
                        .add(hw)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "onSuccess: data added to firestore "+documentReference.getPath());

                                rec_chat.smoothScrollToPosition(adapter.getItemCount() -1 );

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: ");

                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {

            }
        });


        txtinput_msg.getEditText().getText().clear();




    }

    private void setupRecycleView() {
        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", null);

        if(grade==null){
            throw new RuntimeException(" grade should not be null");
        }

        String convoId = getIntent().getStringExtra("EXTRA_CONVO_ID");

        if(convoId==null || convoId.isEmpty()){
            throw new RuntimeException(" convoId should not be null");
        }
        Log.d(TAG, "setupRecycleView: convoId"+convoId);

        collectionReference = db.collection("messages")
                .document(convoId)
                .collection("msg");


        Log.d(TAG, "setupRecycleView: grade"+grade);

        Query query = collectionReference
                .orderBy("timestamp", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class).build();

        String role = "TEACHER";
        adapter = new MessageAdapter(options);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {


                    if (value.size() > 0) {

                        //Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "setupRecycleView: true"+value.toString());



                    } else {
                        Log.d(TAG, "setupRecycleView: false"+value.toString());


                    }

                }


            }
        });

//        adapter.setOnItemClickListner(new MessageAdapter.onItemClickListner() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                String conversationId = documentSnapshot.getId();
//                Log.d(TAG, "onItemClick: "+conversationId);
//                Toast.makeText(getApplicationContext(),"convo"+conversationId,Toast.LENGTH_LONG).show();
//
//                Intent newIntent = new Intent(getApplicationContext(), SingleChat.class);
//
//                startActivity(newIntent);
//
//
//            }
//        });

        rec_chat.setHasFixedSize(true);

        rec_chat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rec_chat.setAdapter(adapter);



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