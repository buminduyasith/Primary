package com.harini.primary.teacher.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.harini.primary.Models.TeacherChatQueue;
import com.harini.primary.R;
import com.harini.primary.SelectParentChat;
import com.harini.primary.SingleChat;
import com.harini.primary.adapters.TeacherChatQueueAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment {

    // onCreateView onViewCreated fragment_teacher_dashboard_chat
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SweetAlertDialog pDialog;
    private TeacherChatQueueAdapter adapter;

    private RecyclerView rec_teacher_chat_queue;
    private CollectionReference collectionReference;
    private FloatingActionButton btnselectuser;

    private String TAG="teacherchat";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_dashboard_chat,container,false);

        setupUi(view);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        init();
        setupRecycleView();

        btnselectuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getContext(), SelectParentChat.class);
                startActivity(newIntent);
            }
        });

        return view;
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("messages");


    }

    private void setupUi(View view) {

        rec_teacher_chat_queue = view.findViewById(R.id.rec_teacher_chat_queue);
        btnselectuser = view.findViewById(R.id.btnselectuser);

    }

    private void setupRecycleView() {
        SharedPreferences prf = this.getActivity().getSharedPreferences("TEACHERS_DATA", MODE_PRIVATE);

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
               // Toast.makeText(getContext(),"convo"+conversationId,Toast.LENGTH_LONG).show();


//                Intent newIntent = new Intent(getContext(), SingleChat.class);
//                newIntent.putExtra("EXTRA_CONVO_ID", conversationId);
//                startActivity(newIntent);
//
//                String parentId = documentSnapshot.getId();
               // Log.d(TAG, "onItemClick: "+documentSnapshot.);
                String name = documentSnapshot.getString("name");
//                Log.d(TAG, "onItemClick: "+parentId);
                //Toast.makeText(getApplicationContext(),"convo"+parentId,Toast.LENGTH_LONG).show();

                Log.d(TAG, "onItemClick: nameee"+name);
                Intent newIntent = new Intent(getContext(), SingleChat.class);
                newIntent.putExtra("EXTRA_CONVO_ID", conversationId);
                newIntent.putExtra("EXTRA_NAME",name );
                startActivity(newIntent);


            }
        });

        rec_teacher_chat_queue.setHasFixedSize(true);

        rec_teacher_chat_queue.setLayoutManager(new LinearLayoutManager(getContext()));

        rec_teacher_chat_queue.setAdapter(adapter);



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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
