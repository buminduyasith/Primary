package com.harini.primary.parent.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.harini.primary.R;
import com.harini.primary.adapters.MessageAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SweetAlertDialog pDialog;
    private MessageAdapter adapter;

    private RecyclerView rec_chat;
    private CollectionReference collectionReference;

    private String TAG="chatparentf";
    private ImageButton btnsend;
    private TextInputLayout txtinput_msg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_parent_dashboard_chat,container,false);

        setupUi(view);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        init();
        setupRecycleView();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    private void setupUi(View view) {

        rec_chat = view.findViewById(R.id.rec_chat);
        btnsend = view.findViewById(R.id.btnsend);
        txtinput_msg = view.findViewById(R.id.txtinput_msg);

    }

    private void sendMsg(){

        SharedPreferences prf = this.getActivity().getSharedPreferences("Parent_DATA", MODE_PRIVATE);

        String grade = prf.getString("GRADE", "null");
        String FIRST_NAME = prf.getString("FIRST_NAME", "null");


        String msg = txtinput_msg.getEditText().getText().toString();
        Timestamp timestamp = new Timestamp(new Date());
        String senderRole = "STUDENT";

        String convoId = mAuth.getCurrentUser().getUid();


        Map<String, Object> recent = new HashMap<>();
        recent.put("grade", grade);
        recent.put("name", FIRST_NAME);
        recent.put("recentMsg", msg);
        recent.put("studentId",convoId);
        recent.put("timestamp", timestamp);

        Map<String, Object> hw = new HashMap<>();
        hw.put("msg", msg);
        hw.put("senderRole", senderRole);
        hw.put("timestamp", timestamp);



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







    }

    private void setupRecycleView() {
//        SharedPreferences prf = getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);
//
//        String grade = prf.getString("GRADE", null);
//
//        if(grade==null){
//            throw new RuntimeException(" grade should not be null");
//        }

        String convoId = mAuth.getCurrentUser().getUid();

        if(convoId==null || convoId.isEmpty()){
            throw new RuntimeException(" convoId should not be null");
        }
        Log.d(TAG, "setupRecycleView: convoId"+convoId);

        collectionReference = db.collection("messages")
                .document(convoId)
                .collection("msg");


        //Log.d(TAG, "setupRecycleView: grade"+grade);

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

                        Log.d(TAG, "setupRecycleView: true"+value.toString());
                        adapter.notifyDataSetChanged();




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

        rec_chat.setLayoutManager(new LinearLayoutManager(getContext()));

        rec_chat.setAdapter(adapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
