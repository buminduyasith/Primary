package com.harini.primary.parent.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.harini.primary.Models.Announcement;
import com.harini.primary.R;
import com.harini.primary.adapters.AnnouncementRecycleViewAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementFragment extends Fragment {


    private RecyclerView recyleview_announcement;
    private LinearLayout announcemtns_noresults_container;




    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    private AnnouncementRecycleViewAdapter adapter;

    private FirestoreRecyclerOptions<Announcement> response;


    private static final String TAG = "AnnouncementFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_dashboard_announcement,container,false);

        setupUi(view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("announcements");

        setupRecycleView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupUi(View view){

        recyleview_announcement = view.findViewById(R.id.recyleview_announcement);
        announcemtns_noresults_container = view.findViewById(R.id.announcemtns_noresults_container);
    }


    private void setupRecycleView(){

       // Query query;

        foo(new Callback() {
            @Override
            public void myResponseCallback(String result) {

                Log.d(TAG, "myResponseCallback: foo"+result);

                Query query = collectionReference.whereEqualTo("grade",result).orderBy("timestamp",Query.Direction.DESCENDING);




                response = new FirestoreRecyclerOptions.Builder<Announcement>()
                        .setQuery(query,Announcement.class).build();


                adapter = new AnnouncementRecycleViewAdapter(response);

                recyleview_announcement.setAdapter(adapter);



                adapter.startListening();
                adapter.notifyDataSetChanged();


                Log.d(TAG, "myResponseCallback: itemcount "+adapter.getItemCount());
                Log.d(TAG, "myResponseCallback: itemcount res "+response.getSnapshots().size());

                /*if(response.getSnapshots().size()>0){

                    announcemtns_noresults_container.setVisibility(View.GONE);
                    recyleview_announcement.setVisibility(View.VISIBLE);
                }
                else{

                    recyleview_announcement.setVisibility(View.GONE);
                    announcemtns_noresults_container.setVisibility(View.VISIBLE);
                }*/






                //adapter.notifyDataSetChanged();



            }


        });


        Query query = collectionReference.whereEqualTo("grade","").orderBy("timestamp",Query.Direction.DESCENDING);

        response = new FirestoreRecyclerOptions.Builder<Announcement>()
                .setQuery(query,Announcement.class).build();


        adapter = new AnnouncementRecycleViewAdapter(response);

        recyleview_announcement.setHasFixedSize(true);

        recyleview_announcement.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        recyleview_announcement.setAdapter(adapter);


        /*if(response.getSnapshots().size()>0){

            announcemtns_noresults_container.setVisibility(View.GONE);
            recyleview_announcement.setVisibility(View.VISIBLE);
        }
        else{

            recyleview_announcement.setVisibility(View.GONE);
            announcemtns_noresults_container.setVisibility(View.VISIBLE);
        }*/







       // Query query = collectionReference.whereEqualTo("grade","").orderBy("timestamp",Query.Direction.DESCENDING);






        //Query query = collectionReference.whereEqualTo("userid",firebaseUser.getUid()).orderBy("timestamp",Query.Direction.DESCENDING);

       /* query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    Log.d(TAG, "onComplete: size"+task.getResult().size());


                }
                else{
                    Log.d(TAG, "onComplete: "+task.getException().getLocalizedMessage());
                }
            }
        });*/





    }

    public void foo(final Callback callback){

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        db.collection("Parents")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        String grade = documentSnapshot.getString("grade");
                        Log.d(TAG, "onSuccess: grade onsucess listner firebase"+grade);
                        callback.myResponseCallback(grade);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });
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

    /*
     db.collection("Parents")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        grade[0] = documentSnapshot.getString("grade");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });
     */

    interface Callback {
        void myResponseCallback(String result);//whatever your return type is: string, integer, etc.
    }
}
